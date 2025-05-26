import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TituloVacioException extends Exception {
    public TituloVacioException(String mensaje) {
        super(mensaje);
    }
}class NoTemaSeleccionadoException extends Exception {
    public NoTemaSeleccionadoException(String mensaje) {
        super(mensaje);
    }
}

public class Formulario {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JCheckBox romanceCheckBox;
    private JCheckBox cienciaFicciónCheckBox;
    private JCheckBox comediaCheckBox;
    private JCheckBox terrorCheckBox;
    private JCheckBox historiaCheckBox;
    private JCheckBox tragediaCheckBox;
    private JButton guardarLibroButton;
    private JButton leerLibrosCargadosButton;
    private JButton terminarButton;
    private JPanel Panel_Principal;
    File archivolibros=new File("Registro_de_libros.dat");
    List<Libro> registrodelibros=new ArrayList<>();

    public Formulario(){
        guardarLibroButton.addActionListener(e -> guardarArchivo());
        leerLibrosCargadosButton.addActionListener(e -> leerLibros());
        terminarButton.addActionListener(e ->finalizar());
    }
    public void guardarArchivo() {
        Libro librohecho=null;
        //Definir array
        ArrayList<Tema> consiste_en = new ArrayList<>();
        Tema tema=null;
        if (romanceCheckBox.isSelected()) {
            consiste_en.add(tema=new Tema("Romance"));
        }
        if (historiaCheckBox.isSelected()) {
            consiste_en.add(tema=new Tema("Historia"));
        }
        if (cienciaFicciónCheckBox.isSelected()) {
            consiste_en.add(tema=new Tema("Ciencia Ficcion"));
        }
        if (comediaCheckBox.isSelected()) {
            consiste_en.add(tema=new Tema("Comedia"));
        }
        if (tragediaCheckBox.isSelected()) {
            consiste_en.add(tema=new Tema("Tragedia"));
        }
        if (terrorCheckBox.isSelected()) {
            consiste_en.add(tema=new Tema("Terror"));
        }

        if (consiste_en.isEmpty()) {
            try {
                throw new NoTemaSeleccionadoException("Error: Debe seleccionar al menos un tema.");
            } catch (NoTemaSeleccionadoException e) {
                JOptionPane.showMessageDialog(Panel_Principal, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
String titulo = textField2.getText().trim();
        if (titulo.length() < 1 || (titulo.length() == 1 && titulo.equals(" "))) {
            try {
                throw new TituloVacioException("Error: El título no puede estar vacío o ser solo un espacio.");
            } catch (TituloVacioException e) {
                JOptionPane.showMessageDialog(Panel_Principal, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        
        //Crear el nuevo libro
        try {
             librohecho = new Libro(textField2.getText(), Integer.parseInt(textField1.getText()), textField3.getText());
             librohecho.setTemas(consiste_en);
        }catch (NumberFormatException e){
            e.getMessage();
            System.out.println("Error: La respuesta del campo 'numero de libro' no es un numero.");
            return;
        }

        //Crear el ObjectInputStream
        if (archivolibros.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivolibros))) {
                registrodelibros = (List<Libro>) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
            registrodelibros.add(librohecho);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivolibros))) {
                oos.writeObject(registrodelibros);
                System.out.println("Libro guardado.");
            } catch (IOException e) {
                System.out.println("Error al escribir el archivo");
                e.printStackTrace();
            }



    }
    public void leerLibros(){
        if (!archivolibros.exists()) {
            JOptionPane.showMessageDialog(Panel_Principal, "No hay registros de libros guardados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            FileInputStream archivoInicial = new FileInputStream(archivolibros);
            ObjectInputStream lectorLibros = new ObjectInputStream(archivoInicial);
            registrodelibros = (List<Libro>) lectorLibros.readObject();
            lectorLibros.close();
            lectorLibros.close();
            String textoLibros = "";
            for (Libro libro : registrodelibros) {
                textoLibros += "Título: " + libro.getTitulo() + "\n";
                textoLibros += "Número: " + libro.getNumerolibro() + "\n";
                textoLibros += "Clasificación: " + libro.getClasificacion() + "\n";
                if (!libro.getTemas().isEmpty()) {
                    textoLibros += "Temas: \n";
                    for (Tema tema : libro.getTemas()) {
                        textoLibros += "- " + tema.getDenominacion() + "\n";
                    }
                }
                textoLibros += "\n";
            }
            JTextArea areaTextoLibros = new JTextArea(textoLibros);
            areaTextoLibros.setEditable(false);
            JScrollPane scroll = new JScrollPane(areaTextoLibros);
            JOptionPane.showMessageDialog(Panel_Principal, scroll, "Libros guardados", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Panel_Principal, "Ocurrió un error al leer el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void finalizar(){
        System.exit(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Formulario");
        frame.setContentPane(new Formulario().Panel_Principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
