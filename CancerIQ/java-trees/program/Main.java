import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Main {

 //All the input files must be in the same folder as the .java and class file
 //Set the input file name here
 public static String inputFileName = "";
 //All the generated output files will be in the same folder as the .java and class file
 //Set the output file name here
 public static String outputFileName = "";

 public static Stack < Node > st;
 public static ArrayList < Node > nodes;
 public static String maxValues;
 public static Boolean found;

 public static void main(String[] args) {
  st = new Stack < Node > ();
  nodes = new ArrayList < Node > ();
  inputFileName=args[0];
  outputFileName=args[1];
  maxValues = "";
  readFile();
  writeFile();
  //System.out.print(maxValues);
 }

 public static void writeFile() {
  try {
   String filePath = new File("").getAbsolutePath();
   filePath = filePath.concat("\\output\\" + outputFileName);
   File file = new File(filePath);
   // if file doesnt exists, then create it
   if (!file.exists()) {
    file.createNewFile();
   }
   FileWriter fw = new FileWriter(file.getAbsoluteFile());
   BufferedWriter bw = new BufferedWriter(fw);
   bw.write(maxValues);
   bw.close();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 public static void createEdge(int n1, int n2) {
  Node start = nodes.get(n1 - 1);
  Node end = nodes.get(n2 - 1);
  Edge e1 = new Edge(start, end);
  Edge e2 = new Edge(end, start);
  start.connections.add(e1);
  end.connections.add(e2);
 }

 public static void readFile() {
  String filePath = new File("").getAbsolutePath();
  filePath = filePath.concat("\\input\\" + inputFileName);
  try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
   String sCurrentLine = br.readLine();

   int n = Integer.parseInt(sCurrentLine);

   for (int i = 0; i < n; i++) {
    nodes.add(new Node(i));
   }

   for (int i = 0; i < n - 1; i++) {
    sCurrentLine = br.readLine();
    String[] result = sCurrentLine.split("\\s");
    int n1 = Integer.parseInt(result[0]);
    int n2 = Integer.parseInt(result[1]);
    createEdge(n1, n2);
   }

   findChildren(0);

   sCurrentLine = br.readLine();

   int q = Integer.parseInt(sCurrentLine);

   for (int i = 0; i < q; i++) {
    sCurrentLine = br.readLine();
    String[] result = sCurrentLine.split("\\s");
    if (result[0].equals("add")) {
     long t1 = System.currentTimeMillis();
     query(0, Integer.parseInt(result[1]), Integer.parseInt(result[2]));
     long t2 = System.currentTimeMillis();
    } else if (result[0].equals("max")) {
     long t1 = System.currentTimeMillis();
     query(1, Integer.parseInt(result[1]), Integer.parseInt(result[2]));
     long t2 = System.currentTimeMillis();
    }
   }
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 public static void unVisitAll() {
  for (int i = 0; i < nodes.size(); i++) {
   nodes.get(i).visited = false;
  }
 }

 public static void printMax() {
  int max = Integer.MIN_VALUE;
  while (!st.empty()) {
   int w = st.pop().weight;
   if (max < w) {
    max = w;
   }
  }
  maxValues += max + "\n";
 }

 public static void query(int c, int n1, int n2) {
  if (c == 0) {
   add(n1, n2);
  } else if (c == 1) {
   st = new Stack < Node > ();
   st.push(nodes.get(n1 - 1));
   nodes.get(n1 - 1).visited = true;
   found = false;
   max(n1, n2);
  }
 }

 public static void add(int index, int value) {
  Node s = nodes.get(index - 1);
  s.weight += value;
  for (int i = 0; i < s.connections.size(); i++) {
   if (s.connections.get(i).end.index != s.parentIndex) {
    add(s.connections.get(i).end.index + 1, value);
   }
  }
 }

 public static void findChildren(int index) {
  Node s = nodes.get(index);
  for (int i = 0; i < s.connections.size(); i++) {
   int myChildIndex = s.connections.get(i).end.index;
   if (myChildIndex != s.parentIndex) {
    setParentIndex(myChildIndex, index);
    findChildren(myChildIndex);
   }
  }
 }

 public static void setParentIndex(int childIndex, int parentIndex) {
  nodes.get(childIndex).parentIndex = parentIndex;
 }
 public static void max(int index1, int index2) {
  if (found == false) {
   if (index1 == index2) {
    found = true;
    printMax();
    unVisitAll();
    return;
   }

   Node s = nodes.get(index1 - 1);
   Node e = nodes.get(index2 - 1);
   for (int i = 0; i < s.connections.size(); i++) {
    if (s.connections.get(i).end.index == e.index) {
     st.push(s.connections.get(i).end);
     found = true;
     printMax();
     unVisitAll();
    } else if (!st.empty()) {
     if (!s.connections.get(i).end.visited && st.peek().index != (index2 - 1)) {
      st.push(s.connections.get(i).end);
      s.connections.get(i).end.visited = true;
      max(s.connections.get(i).end.index + 1, index2);
     }
    }
    if (i == s.connections.size() - 1 && !st.empty()) {
     st.pop();
    }
   }
  }
 }
}

class Node {
 public int weight;
 public int index;
 public int parentIndex;
 public boolean visited = false;
 public ArrayList < Edge > connections = new ArrayList < Edge > ();

 Node(int index) {
  this.index = index;
 }
}

class Edge {
 public Node start;
 public Node end;

 Edge(Node s, Node e) {
  start = s;
  end = e;
 }
}
