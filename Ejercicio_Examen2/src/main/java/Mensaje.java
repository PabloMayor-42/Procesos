import java.io.Serializable;

public record Mensaje (String emisor, String mensaje) implements Serializable{

	@Override
	public final String toString() {
		// TODO Auto-generated method stub
		return emisor + " -> " + mensaje;
	}
}
