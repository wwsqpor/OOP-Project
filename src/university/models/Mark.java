package university.models;

import java.io.Serializable;

public class Mark implements Serializable {
    private final double attestation1;
    private final double attestation2;
    private final double finalExam;

    public Mark(double attestation1, double attestation2, double finalExam) {
        this.attestation1 = clamp(attestation1, 0, 30);
        this.attestation2 = clamp(attestation2, 0, 30);
        this.finalExam = clamp(finalExam, 0, 40);
    }

    public double total() {
        return attestation1 + attestation2 + finalExam;
    }

    public boolean isFail() {
        return total() < 50;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public String toString() {
        return String.format("A1=%.1f A2=%.1f Final=%.1f Total=%.1f", attestation1, attestation2, finalExam, total());
    }
}
