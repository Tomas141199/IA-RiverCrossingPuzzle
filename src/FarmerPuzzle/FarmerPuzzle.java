package FarmerPuzzle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class FarmerPuzzle {

    // Arreglo con los posibles MOVIMIENTOS que el granjero puede hacer
    private String[] movimientos = {"G", "GL", "GO", "GC"};
    private ArrayList<Node> queue;
    // Arreglo con la(s) solucion(es) encontradas
    private ArrayList<Node> soluciones;
    private ArrayList<String> solucion;

    private Node root;

    //Incializacion de los atributos queue y soluciones
    public FarmerPuzzle() {
        queue = new ArrayList<Node>();
        soluciones = new ArrayList<Node>();
        solucion = new ArrayList<String>();
    }

    /**
     * Clase interna anidada privada para representar un ESTADO, un estado
     * consta de los ocupantes (granjero, lobo, oveja, col) en la orilla
     * izquierda y derecha del rio, Una de las orillas (izquierda o derecha) es
     * donde el granjero se encuntra y desde ahi el granjero puede cruzar el rio
     * hasta la orilla opuesta, opcionalmente trayendo con el algun
     * ocupante(lobo, oveja, col)
     *
     *
     */
    /**
     * Se asume que el estado inicial es: banco izquierdo: Granjero, Lobo, Col,
     * Oveja <-> banco derecho <vacio>
     * (Esto se va a poder modificar desde la interfa de usuario) Todos los
     * ocupantes incluyendo el granjero se encuentran en alguno de los dos
     * bancos(izq,der) y el otro permanece vacio o con alguno ocupante El
     * granjero intentara mover a todos al banco derecho a traves de una
     * secuencia de cruces los cuales se encuentran sujetos a restricciones del
     * problema. El granjero solo puede mover como maximo a un ocupante ademas
     * de el mismo. La col y la oveja no pueden estar solas sin el granjero al
     * igual que que la oveja y el lobo
     *
     * El estado solucion del problema es: banco izquierdo - (vacio) <-> banco
     * derecho - granjero, lobo, col y oveja
     */
    private class State {

        //El banco activo en donde el granjero se encuentra localizado
        private String bank;
        //Bancos izquierdo y derecho con sus ocupantes
        private TreeSet<String> left, right;

        //Inicializacion de valores de acuerdo a los paramentros proporcionados por la interfaz/inicializacion manual
        public State(String bank, TreeSet<String> left, TreeSet<String> right) {
            this.bank = bank;
            this.left = left;
            this.right = right;
        }

        /**
         * Toma un Arbol de strings que contiene los ocupantes en los bancos del
         * rio(izquierdo o derecho) y verifica las restricciones propuestas
         *
         * El parametro b representa un banco del rio con sus ocupantes La
         * funcion retorna true si el problema presenta las retricciones y flaso
         * en caso contrario
         */
        private boolean checkAllowBank(TreeSet<String> b) {
            //El lobo y la oveja se encuentra juntos sin el granjero
            if (b.contains("L") && b.contains("O") && (b.contains("G") == false)) {
                return false;
            }
            //La oveja y la col juntas sin el granjero
            if (b.contains("O") && b.contains("C") && (b.contains("G") == false)) {
                return false;
            }

            return true;
        }

        /**
         * Metodo public que verifica si un estado cumple las restricciones Los
         * estados de rechazo son cuando no cumplen las restricciones
         *
         * Retorna true si un estado esta permitido, falso en caso contrario
         */
        public boolean isAllow() {
            if (checkAllowBank(left) && checkAllowBank(right)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Verifica el estado de la solucion donde se resuelve el problema
         */
        public boolean isSolution() {
            if (left.isEmpty() && right.contains("L") && right.contains("O") && right.contains("C")
                    && right.contains("G")) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Transita a un nuevo estado secundario de acuerdo al movimiento
         *
         * El parametro movimieto contiene alguno de los moviemientos permitidos
         * (G, GL, GO, GC) para transitar a un nuevo Estado hijo
         *
         * Retorna un State, un nuevo hijo State baso en la transicion o nulo si
         * el movimiento no es valido
         */
        public State transits(String move) {
            String nbank;
            TreeSet<String> nleft = new TreeSet<String>();
            TreeSet<String> nright = new TreeSet<String>();

            if (bank.equalsIgnoreCase("left")) {
                nbank = "right";
            } else {
                nbank = "left";
            }

            copylist(right, nright);
            copylist(left, nleft);

            for (int i = 0; i < move.length(); i++) {
                String item = move.substring(i, i + 1);
                if (bank.equalsIgnoreCase("left")) {
                    if (nleft.remove(item)) {
                        nright.add(item);
                    } else {
                        //Retorna null si el movimiento contiene ocupantes que no estan presentes
                        return null;
                    }
                } else {
                    if (nright.remove(item)) {
                        nleft.add(item);
                    } else {
                        //Retorna null si el movimiento contiene ocupantes que no estan presentes
                        return null;
                    }
                }
            }

            return new State(nbank, nleft, nright);

        }

        /**
         * Metodo que duplica una representacion de alguno de los bancos del rio
         * y sus ocupantes desde el origen hasta el destino
         */
        private void copylist(TreeSet<String> src, TreeSet<String> dst) {
            for (String e : src) {
                dst.add(e);
            }
        }

        /**
         * Compara un estado actual con un estado en especifico
         *
         * s es el parametro para comparar Retorna true si el estado actual y el
         * especificado son el mismo, false en caso contrario
         *
         */
        public boolean compare(State s) {
            TreeSet<String> tmp;

            if (!s.getBank().equalsIgnoreCase(bank)) {
                return false;
            }

            tmp = s.getLeft();
            for (String e : left) {
                if (!tmp.contains(e)) {
                    return false;
                }
            }

            tmp = s.getRight();
            for (String e : right) {
                if (!tmp.contains(e)) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Metodos Get de la clase principal
         */
        public String getBank() {
            return bank;
        }

        public TreeSet<String> getLeft() {
            return left;
        }

        public TreeSet<String> getRight() {
            return right;
        }

        /**
         *
         * Sobrecarga del metodo de impresion para la visualizacion de los
         * resultados
         *
         */
        @Override
        public String toString() {
            StringBuffer ret = new StringBuffer();
            ret.append("{Izq:");

            for (String e : left) {
                ret.append(e);
            }

            ret.append(" ");
            ret.append("Der:");

            for (String e : right) {
                ret.append(e);
            }

            ret.append("}");
            return ret.toString();
        }

    }

    /**
     *
     * Clase privada anidada para la construccion del grafo de estados
     *
     */
    private class Node {

        // Padre del nodo
        public Node parent;
        // State del nodo
        public State data;
        // Hijos del nodo
        public ArrayList<Node> adjlist;
        //Nivel del nodo
        public int level;
        // El movimiento que crea el actual estado del nodo
        public String move;

        public Node(State data) {
            parent = null;
            this.data = data;
            adjlist = new ArrayList<Node>();
            level = 0;
            move = "";
        }

        /**
         * Verifica si un nodo que tiene el mismo estado es un antecesor del
         * nodo actual
         *
         * Retorna true si un nodo antecesor tiene el mismo estado, falso de
         * otra manera
         */
        public boolean isAncestor() {
            Node n = parent;
            boolean ret = false;
            while (n != null) {
                if (data.compare(n.data)) {
                    ret = true;
                    break;
                }

                n = n.parent;
            }

            return ret;
        }

    }

    /**
     * Metodo que inicia la creacion del grafo de estados usando profundidad,
     * transitando entre los estados permitidos
     */
    public void startBreadthFirstSearch(String bank, TreeSet<String> left, TreeSet<String> right) {
        //Se inicializan las solucioens en 0
        soluciones = new ArrayList<Node>();
        State inits = new State(bank, left, right);
        root = new Node(inits);
        root.level = 0;
        queue.add(root);

        while (!queue.isEmpty()) {
            Node n = queue.remove(0);
            System.out.println("Procesando Nivel: " + n.level + " " + n.data);
            for (String m : movimientos) {

                State s = n.data.transits(m);

                //Verifica si es un estado permitido
                if (s != null && s.isAllow()) {

                    Node child = new Node(s);
                    child.parent = n;
                    child.level = n.level + 1;
                    child.move = m + " movimientos " + child.data.getBank();

                    //Verifica que el nodo actual no es  antecesor, para evitar ciclos
                    if (!child.isAncestor()) {
                        n.adjlist.add(child);

                        if (child.data.isSolution() == false) {
                            queue.add(child);

                            System.out.println("Agregando estado " + child.data);
                        } else {
                            soluciones.add(child);
                            System.out.println("Solucion encontrada " + child.data);
                            return;
                        }
                    }

                }

            }

        }
    }

    /**
     * Imprime el grafo entero de estados usando la busqueda en profundidad
     */
    public void printBFSGraph() {
        ArrayList<Node> queue = new ArrayList<Node>();

        queue.add(root);

        while (!queue.isEmpty()) {
            Node n = queue.remove(0);
            System.out.println("Nivel " + n.level + " - " + n.data);

            ArrayList<Node> adjlist = n.adjlist;
            for (Node e : adjlist) {
                queue.add(e);
            }
        }
    }

    /**
     * Genera una cadena entera con el grafo de estados para la busqueda en
     * profundidad
     */
    public String toStringBFSGraph() {
        ArrayList<Node> queue = new ArrayList<Node>();
        String result = "";
        queue.add(root);

        while (!queue.isEmpty()) {
            Node n = queue.remove(0);
            result += "Nivel " + n.level + " - " + n.data + "\n\n";
            ArrayList<Node> adjlist = n.adjlist;
            for (Node e : adjlist) {
                queue.add(e);
            }
        }
        return result;
    }

    /**
     * Imprime los solucioes, estados y los movimientos que llevan las
     * soluciones
     */
    public void printSolution() {
        System.out.println("No. de soluciones:  " + soluciones.size());
        ArrayList<Node> stack;

        Iterator<Node> iter = soluciones.iterator();
        int i = 1;
        while (iter.hasNext()) {
            stack = new ArrayList<Node>();
            Node n = iter.next();
            stack.add(n);

            n = n.parent;
            while (n != null) {
                stack.add(n);
                n = n.parent;
            }
            System.out.println("Solucion " + i);
            printSequence(stack);
            i++;
        }
    }

    public ArrayList<String> getSolutionBFS() {
        ArrayList<Node> stack;
        Iterator<Node> iter = soluciones.iterator();
        int i = 1;
        while (iter.hasNext()) {
            stack = new ArrayList<Node>();
            Node n = iter.next();
            stack.add(n);

            n = n.parent;
            while (n != null) {
                stack.add(n);
                n = n.parent;
            }
            for (int j = stack.size() - 1; j >= 0; j--) {
                Node node = stack.get(i);
                if (j != 0) {
                    solucion.add(stack.get(j - 1).move);
                }
            }
            i++;
        }
        return solucion;
    }

    private void printSequence(ArrayList<Node> stack) {
        StringBuffer buf = new StringBuffer();
        buf.append("No. de Movimientos: ");
        buf.append(stack.size() - 1);
        buf.append("\n");
        for (int i = stack.size() - 1; i >= 0; i--) {
            Node n = stack.get(i);
            buf.append(n.data.toString());
            if (i != 0) {
                buf.append("--");
                buf.append(stack.get(i - 1).move);
                buf.append("->>");
            }
        }

        System.out.println(buf.toString());

    }
    
    public void clearSearch(){
        root = null;
        solucion.clear();
        soluciones.clear();
        queue.clear();
    }

}
