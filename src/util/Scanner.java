package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Scanner {

    private BufferedReader br;
    private StringTokenizer st;

    private String seperator;

    public Scanner(InputStream is) {
        br = new BufferedReader(new InputStreamReader(is));
    }

    public Scanner(String pStrFileName, String pStrSeperator) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(new File(pStrFileName)));
        this.seperator = pStrSeperator;
    }

    public String next() throws IOException {
        checkEmptyST();
        return st.nextToken();
    }

    public String nextLine() throws IOException {
        return br.readLine();
    }

    public int nextInt() throws IOException {
        checkEmptyST();
        return Integer.parseInt(st.nextToken());
    }

    public boolean ready() throws IOException {
        return br.ready();
    }

    private void checkEmptyST() throws IOException {
        if (st == null || !st.hasMoreTokens()) {
            if (seperator != null) {
                st = new StringTokenizer(br.readLine(), seperator);
            } else {
                st = new StringTokenizer(br.readLine());
            }
        }
    }
}