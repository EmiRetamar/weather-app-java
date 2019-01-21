package entrega1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(
	     urlPatterns={"/ImagenClima"},
	     initParams={
	    		    @WebInitParam(name="iniciaDia", value="6"),
	    		    @WebInitParam(name="iniciaNoche",value="20"),
	    		    @WebInitParam(name="calor",value="25"),
	    		    @WebInitParam(name="frio",value="14")}
	     )
public class ImagenClima extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    public ImagenClima() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Tipo de contenido a devolver
		response.setContentType("image/jpeg");
		String idioma = (String) request.getAttribute("idioma");
		ServletContext context = request.getServletContext();
		String url = (String) context.getAttribute("url");
		URL urlJson = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlJson.openStream(), "UTF-8"));
		String documentoJSON = in.readLine();
		JSONParser jp = new JSONParser();
		try {
			JSONObject json = (JSONObject) jp.parse(documentoJSON);
			// Instancia de Calendar para obtener la hora
			Calendar calendario = Calendar.getInstance();
			// Archivo donde se escribira la imagen luego de editarse
			OutputStream salida = response.getOutputStream();
			// Si es de dia
			if(calendario.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(this.getServletConfig().getInitParameter("iniciaDia")) && calendario.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(this.getServletConfig().getInitParameter("iniciaNoche"))) {
				// Si hace calor
				if(((Long) json.get("temperature")).intValue() >= Integer.parseInt(this.getServletConfig().getInitParameter("calor"))) {
				// Imagen de dia caluroso
					this.editarImagen("Imagenes/dia_calor.jpg", salida, json, idioma, "dia");
					System.out.println("Imagen de dia caluroso");
				}
				else {
					if(((Long) json.get("temperature")).intValue() < Integer.parseInt(this.getServletConfig().getInitParameter("frio"))) {
						// Imagen de dia frio
						this.editarImagen("Imagenes/dia_frio.jpg", salida, json, idioma, "dia");
						System.out.println("Imagen de dia frio");
					}
					else {
						// Imagen de dia templado
						this.editarImagen("Imagenes/dia_templado.jpg", salida, json, idioma, "dia");
						System.out.println("Imagen de dia templado");
					}
				}
			}
			// Si es de noche
			else {
				// Si hace calor
				// The .intValue() method is defined in class Number, which Long extends
				if(((Long) json.get("temperature")).intValue() >= Integer.parseInt(this.getServletConfig().getInitParameter("calor"))) {
					// Imagen de noche calurosa
					this.editarImagen("Imagenes/noche_calor.jpg", salida, json, idioma, "noche");
					System.out.println("Imagen de noche calurosa");
				}
				else {
					if(((Long) json.get("temperature")).intValue() < Integer.parseInt(this.getServletConfig().getInitParameter("frio"))) {
						// Imagen de noche fria
						this.editarImagen("Imagenes/noche_frio.jpg", salida, json, idioma, "noche");
						System.out.println("Imagen de noche fria");
					}
					else {
						// Imagen de noche templada
						this.editarImagen("Imagenes/noche_templado.jpg", salida, json, idioma, "noche");
						System.out.println("Imagen de noche templada");
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void editarImagen(String pathImagen, OutputStream salida, JSONObject json, String idioma, String estadoDia) throws IOException {
		BufferedImage imagen = ImageIO.read(this.getServletContext().getResourceAsStream(pathImagen)); // Obtiene la imagen
		Graphics2D g2d = (Graphics2D) imagen.getGraphics(); // Imagen preparada para editar
		// Segun el idioma cambian las coordenadas de ubicacion de los textos
		// Idioma de mi navegador: es-419,es;q=0.8,en;q=0.6
		if(idioma.equals("es-ES")) {
			ResourceBundle bundle = ResourceBundle.getBundle("recursos.textos_español");
			// Edicion de la imagen
			g2d.drawString(bundle.getString("titulo"), 0, 10); // String en la coordenada 150,80
			// Para convertir de entero a string hay 2 posibilidades: String.valueOf(numero) o Integer.toString(numero)
			g2d.drawString((String) json.get("captured_at"), 150, 40);
			// Para que se note el texto de la imagen, si la imagen es de dia se setea un color oscuro sino se setea un color claro
			if(estadoDia.equals("dia"))
				g2d.setColor(Color.BLUE);
			else
				g2d.setColor(Color.YELLOW);
			g2d.drawString(bundle.getString("temperatura") + " " + String.valueOf((Long) json.get("temperature")) + " °C", 10, 105);
			// El color por defecto vuelve a ser blanco como en la imagen de ejemplo
			g2d.setColor(Color.WHITE);
			g2d.drawString(bundle.getString("humedad") + ": " + String.valueOf((Long) json.get("humidity")) + "%", 10, 150);
			g2d.drawString(bundle.getString("velocidad_viento") + ": " + String.valueOf((Long) json.get("wind_speed")) + " km/h", 10, 170);
			g2d.drawString(bundle.getString("presion_atmosferica") + ": " + String.valueOf((Long) json.get("bar")) + " hPa", 195, 150);
			g2d.drawString(bundle.getString("direccion_viento") + ": " + (String) json.get("wind_direction"), 233, 170);
		}
		else {
			ResourceBundle bundle = ResourceBundle.getBundle("recursos.textos_ingles");
			// Edicion de la imagen
			g2d.drawString(bundle.getString("titulo"), 0, 10); // String en la coordenada 150,80
			// Para convertir de entero a string hay 2 posibilidades: String.valueOf(numero) o Integer.toString(numero)
			g2d.drawString((String) json.get("captured_at"), 150, 40);
			// Para que se note el texto de la imagen, si la imagen es de dia se setea un color oscuro sino se setea un color claro
			if(estadoDia.equals("dia"))
				g2d.setColor(Color.BLACK);
			else
				g2d.setColor(Color.GREEN);
			g2d.drawString(bundle.getString("temperatura") + " " + String.valueOf((Long) json.get("temperature")) + " °C", 10, 105);
			// El color por defecto vuelve a ser blanco como en la imagen de ejemplo
			g2d.setColor(Color.WHITE);
			g2d.drawString(bundle.getString("humedad") + ": " + String.valueOf((Long) json.get("humidity")) + "%", 10, 150);
			g2d.drawString(bundle.getString("velocidad_viento") + ": " + String.valueOf((Long) json.get("wind_speed")) + " km/h", 10, 170);
			g2d.drawString(bundle.getString("presion_atmosferica") + ": " + String.valueOf((Long) json.get("bar")) + " hPa", 265, 150);
			g2d.drawString(bundle.getString("direccion_viento") + ": " + (String) json.get("wind_direction"), 265, 170);
		}
		ImageIO.write(imagen, "jpg", salida); // Escribe la imagen en la salida
		salida.close(); // Termina la respuesta
	}

}
