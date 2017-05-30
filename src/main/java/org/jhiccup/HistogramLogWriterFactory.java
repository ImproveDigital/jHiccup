/**
 * Written by Szabolcs Pota of ImproveDigital, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Szabolcs Pota
 */
package org.jhiccup;

import org.HdrHistogram.EncodableHistogram;
import org.HdrHistogram.HistogramLogWriter;

import java.io.PrintStream;

/**
 * Create either an original {@link HistogramLogWriter} or a noop log writer that essentially disables logging.
 */
public class HistogramLogWriterFactory {

    private static final class NoOpHistogramLogWriter implements IHistogramLogWriter {

        private long baseTime;

        @Override
        public void outputIntervalHistogram(double startTimeStampSec, double endTimeStampSec,
            EncodableHistogram histogram, double maxValueUnitRatio) {}

        @Override
        public void outputIntervalHistogram(double startTimeStampSec, double endTimeStampSec,
            EncodableHistogram histogram) {}

        @Override
        public void outputIntervalHistogram(EncodableHistogram histogram) {}

        @Override
        public void outputStartTime(long startTimeMsec) {}

        @Override
        public void outputBaseTime(long baseTimeMsec) {}

        @Override
        public void outputComment(String comment) {}

        @Override
        public void outputLegend() {}

        @Override
        public void outputLogFormatVersion() {}

        @Override
        public void setBaseTime(long baseTimeMsec) {
            this.baseTime = baseTimeMsec;
        }

        @Override
        public long getBaseTime() {
            return baseTime;
        }
    }

    private static final class HistogramLogWriterAdapter implements IHistogramLogWriter {
        HistogramLogWriter delegate;

        public HistogramLogWriterAdapter(HistogramLogWriter delegate) {
            this.delegate = delegate;
        }

        public void outputIntervalHistogram(double startTimeStampSec, double endTimeStampSec,
            EncodableHistogram histogram, double maxValueUnitRatio) {
            delegate.outputIntervalHistogram(startTimeStampSec, endTimeStampSec, histogram, maxValueUnitRatio);
        }

        public void outputIntervalHistogram(double startTimeStampSec, double endTimeStampSec,
            EncodableHistogram histogram) {
            delegate.outputIntervalHistogram(startTimeStampSec, endTimeStampSec, histogram);
        }

        public void outputIntervalHistogram(EncodableHistogram histogram) {
            delegate.outputIntervalHistogram(histogram);
        }

        public void outputStartTime(long startTimeMsec) {
            delegate.outputStartTime(startTimeMsec);
        }

        public void outputBaseTime(long baseTimeMsec) {
            delegate.outputBaseTime(baseTimeMsec);
        }

        public void outputComment(String comment) {
            delegate.outputComment(comment);
        }

        public void outputLegend() {
            delegate.outputLegend();
        }

        public void outputLogFormatVersion() {
            delegate.outputLogFormatVersion();
        }

        public void setBaseTime(long baseTimeMsec) {
            delegate.setBaseTime(baseTimeMsec);
        }

        public long getBaseTime() {
            return delegate.getBaseTime();
        }
    }

    public static IHistogramLogWriter noOpHistogramLogWriter() {
        return new NoOpHistogramLogWriter();
    }

    public static IHistogramLogWriter histogramLogWriter(PrintStream printStream) {
        return new HistogramLogWriterAdapter(new HistogramLogWriter(printStream));
    }
}
