package com.cryptopurse.cryptotrader.market.timeseries;

import com.cryptopurse.cryptotrader.market.domain.TradeHistory;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class TimeseriesBuilder {

    public TimeSeries timeseries(List<TradeHistory> trades, int timeFrameInSeconds) {
        List<Tick> ticks;
        Optional<TradeHistory> firstTrade = trades
                .stream()
                .sorted(Comparator.comparing(TradeHistory::getTime))
                .findFirst();
        Optional<TradeHistory> lastTrade = trades
                .stream()
                .sorted((x1, x2) -> x2.getTime().compareTo(x1.getTime()))
                .findFirst();

        ticks = buildEmptyTicks(new DateTime(firstTrade.get().getTime()), new DateTime(lastTrade.get().getTime()), timeFrameInSeconds);

        for (TradeHistory trade : trades) {
            DateTime tradeTimestamp = new DateTime(trade.getTime());
            for (Tick tick : ticks) {
                if (tick.inPeriod(tradeTimestamp)) {
                    BigDecimal tradePrice = trade.getPrice();
                    BigDecimal tradeAmount = trade.getAmount();
                    tick.addTrade(tradeAmount.doubleValue(), tradePrice.doubleValue());
                }
            }
        }

        // Removing still empty ticks
        removeEmptyTicks(ticks);
        return new TimeSeries(ticks);
    }

    private static void removeEmptyTicks(List<Tick> ticks) {
        for (int i = ticks.size() - 1; i >= 0; i--) {
            if (ticks.get(i).getTrades() == 0) {
                ticks.remove(i);
            }
        }
    }

    private static List<Tick> buildEmptyTicks(DateTime beginTime, DateTime endTime, int duration) {

        List<Tick> emptyTicks = new ArrayList<>();

        Period tickTimePeriod = Period.seconds(duration);
        DateTime tickEndTime = beginTime;
        do {
            tickEndTime = tickEndTime.plus(tickTimePeriod);
            emptyTicks.add(new Tick(tickTimePeriod, tickEndTime));
        } while (tickEndTime.isBefore(endTime));

        return emptyTicks;
    }

}
