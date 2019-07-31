public enum EnergyType {

    /**
     * Renewable energy
     */
    CCGT("ccgt", true), WIND("wind", true), PUMPED("pumped", true),
    HYDRO("hydro", true), OCGT("ocgt", true), SOLAR("solar", true),

    /**
     * Non-renewable energy
     */
    COAL("coal"), NUCLEAR("nuclear"), OIL("oil"), FRENCH_ICT("french_ict"),
    DUTCH_ICT("dutch_ict"), IRISH_ICT("irish_ict"), EW_ICT("ew_ict"), OTHER("other");

    private final String name;
    private final boolean renewable;

    private EnergyType(String name) {
        this(name, false);
    }

    /**
     * using this method sub item into name and renewable object
     */
    private EnergyType(String name, boolean renewable) {
        this.name = name;
        this.renewable = renewable;
    }

    /**
     * boolean method return true or false
     */
    public boolean isRenewable() {
        return renewable;
    }

    public String getName() {
        return name;
    }

}
