package spinner.astronomy;

public class CelestialBody {
    public final String name;
    public final double azimuth;
    public final double altitude;

    public CelestialBody(String name, double azimuth, double altitude) {
        this.name = name;
        this.azimuth = azimuth;
        this.altitude = altitude;
    }
}
