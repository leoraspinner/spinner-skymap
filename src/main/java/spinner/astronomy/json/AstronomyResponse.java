package spinner.astronomy.json;

import javax.swing.text.Position;
import javax.xml.crypto.Data;

public class AstronomyResponse {
    public Data data;

    public class Data {
        public Position position;
    }

    public class Position {
        public Horizontal horizontal;
    }

    public class Horizontal
    {
        public double altitude;
        public double azimuth;
    }
}
