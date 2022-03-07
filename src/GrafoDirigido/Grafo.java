package GrafoDirigido;


import javax.swing.*;
import java.util.LinkedList;

public class Grafo {

    private Lista LVertices;

    public Grafo() {
        LVertices = new Lista();
    }

    public void crearVertice(String nomV) {
        LVertices.insertarUlt(new Vertice(nomV));
    }

    public Vertice buscarVertice(String nomV) {
        Vertice vertice;
        int i = 0;
        while (i < LVertices.dim()) {
            vertice = (Vertice) LVertices.getElem(i);
            if (vertice.getNombre().equals(nomV)) {
                return vertice;
            }
            i++;
        }
        return null;
    }

    public void insertarArco(String X, String Y, float co) {
        Vertice vo = buscarVertice(X);
        Vertice vd = buscarVertice(Y);
        vo.insertarArco(new Arco(vd, co));
    }

    public void imprimir(JTextArea jta) {
        int i = 0;
        Vertice v;
        Arco a;
        while (i < LVertices.dim()) {
            v = (Vertice) LVertices.getElem(i);
            int j = 0;
            while (j < v.LArcos.dim()) {
                jta.append(v.getNombre());
                jta.append("-->");
                a = (Arco) v.LArcos.getElem(j); //Muestra el arco donde apunto
                jta.append(a.getNombreVertD() + "  " + a.getCosto());
                jta.append("\n");
                j++;
            }
            i++;
        }
    }

    public float peso() {
        int i = 0;
        Vertice v;
        float s = 0;
        while (i < LVertices.dim()) {
            v = (Vertice) LVertices.getElem(i);
            int j = 0;
            Arco a;
            while (j < v.LArcos.dim()) {
                a = (Arco) v.LArcos.getElem(j);
                s = s + a.getCosto();
                j++;
            }
            i++;
        }
        return s;
    }

    public void desmarcarTodos() {
        for (int i = 0; i < this.LVertices.dim(); i++) {
            Vertice v = (Vertice) this.LVertices.getElem(i);
            v.marcado = false;
        }
    }

    public void ordenarVerticesAlf() {
        Vertice aux;
        Vertice v1;
        Vertice v2;
        for (int i = 0; i < LVertices.dim(); i++) {
            for (int j = 0; j < LVertices.dim() - 1; j++) {
                v1 = (Vertice) LVertices.getElem(j);
                v2 = (Vertice) LVertices.getElem(j + 1);
                if (v1.getNombre().compareTo(v2.getNombre()) > 0) {
                    aux = (Vertice) LVertices.getElem(j);
                    LVertices.setElem(v2, j);
                    LVertices.setElem(aux, j + 1);
                }
            }
        }
        for (int i = 0; i < LVertices.dim(); i++) {
            Vertice v = (Vertice) LVertices.getElem(i);
            v.ordenarArcosAlf();
        }
    }

    public void DFS(String A, JTextArea jta) {
        jta.append("DFS: ");
        desmarcarTodos();
        ordenarVerticesAlf();
        Vertice v = buscarVertice(A);
        dfs(v, jta);
        jta.append("\n");
    }

    private void dfs(Vertice v, JTextArea jta) {
        jta.append(v.getNombre() + " ");//mostrar
        v.marcado = true;//marca
        Arco a;
        for (int i = 0; i < v.LArcos.dim(); i++) {
            a = (Arco) v.LArcos.getElem(i);
            Vertice w = buscarVertice(a.getNombreVertD());
            if (!w.marcado) {
                dfs(w, jta);
            }
        }
    }

    public void BFS(String s, JTextArea jta) {
        desmarcarTodos();
        ordenarVerticesAlf();
        Arco a;
        Vertice v = buscarVertice(s), w;
        LinkedList<Vertice> C; //cola
        C = new LinkedList<>();
        C.add(v);//anhade
        v.marcado = true;//marca
        jta.append("BFS: ");
        //hacer hasta que la cola este vacia
        do {
            v = C.pop();//toma y borra el primero
            jta.append(v.getNombre() + " ");//mostrar
            //insertar los adyacentes no marcados
            for (int i = 0; i < v.LArcos.dim(); i++) {
                a = (Arco) v.LArcos.getElem(i);
                w = buscarVertice(a.getNombreVertD());
                if (!w.marcado) {
                    C.add(w);
                    w.marcado = true;
                }
            }
        } while (!C.isEmpty());

        jta.append("\n");
    }

    public boolean existeCamino(String A, String B) {
        desmarcarTodos();
        Vertice vo = buscarVertice(A);
        Vertice vd = buscarVertice(B);
        return existeCamino(vo, vd);
    }

    private boolean existeCamino(Vertice vo, Vertice vd) {
        boolean b = false;
        Arco a;
        //caso base: (A,A)
        if (vo.getNombre().equals(vd.getNombre())) {
            return true;
        }
        vo.marcado = true;
        for (int i = 0; i < vo.LArcos.dim(); i++) {
            //adyacentes de vo
            a = (Arco) vo.LArcos.getElem(i);
            //un vertice adyacente
            Vertice w = buscarVertice(a.getNombreVertD());
            //ya pase por aca, no pille el camino
            if (w.marcado) {
                return false;
            }
            //pregunta si w es el destino
            if (vd.getNombre().equals(w.getNombre())) {
                return true;
            }
            b = b || existeCamino(w, vd);
            w.marcado = false;
        }

        return b;
    }

    public int cantidadCaminos(String A, String B) {
        desmarcarTodos();
        Vertice vo = buscarVertice(A);
        Vertice vd = buscarVertice(B);
        return cantidadCaminos(vo, vd);
    }

    private int cantidadCaminos(Vertice vo, Vertice vd) {
        int c = 0;
        Arco a;
        vo.marcado = true;
        //llega a vertice destino
        if (vo.getNombre().equals(vd.getNombre())) {
            vd.marcado = false;
            return 1;
        }
        //caso general de recursion 
        for (int i = 0; i < vo.getCantArcos(); i++) {
            a = (Arco) vo.LArcos.getElem(i);
            Vertice w = buscarVertice(a.getNombreVertD());
            if (!w.marcado && existeCamino(w, vd)) {
                c = c + (1 * cantidadCaminos(w, vd));
            }
            w.marcado = false;
        }
        return c;
    }

}



//end class
