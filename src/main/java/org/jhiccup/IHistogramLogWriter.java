/**
 * Written by Szabolcs Pota of ImproveDigital, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Szabolcs Pota
 */
package org.jhiccup;

import org.HdrHistogram.EncodableHistogram;
import org.HdrHistogram.HistogramLogWriter;


/**
 * An interface extract from {@link org.HdrHistogram.HistogramLogWriter} so that it can be easily replaced in
 * {@link HiccupMeter}.
 */
public interface IHistogramLogWriter {

    /**
     * @see HistogramLogWriter#outputIntervalHistogram(double, double, EncodableHistogram, double)
     */
    public void outputIntervalHistogram(final double startTimeStampSec,
                                        final double endTimeStampSec,
                                        final EncodableHistogram histogram,
                                        final double maxValueUnitRatio);

    /**
     * @see HistogramLogWriter#outputIntervalHistogram(double, double, EncodableHistogram)
     */
    public void outputIntervalHistogram(final double startTimeStampSec,
                                        final double endTimeStampSec,
                                        final EncodableHistogram histogram);

    /**
     * @see HistogramLogWriter#outputIntervalHistogram(EncodableHistogram)
     */
    public void outputIntervalHistogram(final EncodableHistogram histogram);

    /**
     * @see HistogramLogWriter#outputStartTime(long)
     */
    public void outputStartTime(final long startTimeMsec);


    /**
     * @see HistogramLogWriter#outputBaseTime(long)
     */
    public void outputBaseTime(final long baseTimeMsec);

    /**
     * @see HistogramLogWriter#outputComment(String)
     */
    public void outputComment(final String comment);

    /**
     * @see HistogramLogWriter#outputLegend()
     */
    public void outputLegend();

    /**
     * @see HistogramLogWriter#outputLogFormatVersion()
     */
    public void outputLogFormatVersion();

    /**
     * @see HistogramLogWriter#setBaseTime(long)
     */
    public void setBaseTime(long baseTimeMsec);

    /**
     * @see HistogramLogWriter#getBaseTime()
     */
    public long getBaseTime();
}
