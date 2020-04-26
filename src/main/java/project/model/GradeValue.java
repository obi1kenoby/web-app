package project.model;

/**
 * Simple JavaBean object that represents
 * the value of {@link Grade} from A to F.
 * and extended {@link Model} class.
 *
 * @author Alexander Naumov.
 */
public enum GradeValue {

    /**
     * 90%–100%
     */
    A,
    /**
     * 80%–89%
     */
    B,
    /**
     * 70%–79%
     */
    C,
    /**
     * 60%–69%
     */
    D,
    /**
     * < 60%
     */
    F;

    /**
     * Returns {@link GradeValue} by percent value.
     *
     * @param percent grade in percents.
     * @return {@link GradeValue}.
     */
    public static GradeValue getValueFromPercent(double percent) {
        if (percent < 60) {
            return F;
        } else if (percent >= 60 && percent < 70) {
            return D;
        } else if (percent >= 70 && percent < 80) {
            return C;
        } else if (percent >= 80 && percent < 90) {
            return B;
        } else {
            return A;
        }
    }
}
