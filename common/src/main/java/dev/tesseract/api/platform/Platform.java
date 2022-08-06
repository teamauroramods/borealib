package dev.tesseract.api.platform;

public final class Platform {

    private Platform() {
    }

    /**
     * Throws an {@link AssertionError}.
     */
    public static <T> T error() {
        throw new AssertionError();
    }
}
