package ua.moses.Integration;

import java.io.*;

public class ConsoleMock {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    public ConsoleMock() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    public void addIn(String line) {
        in.add(line);
    }

    public String getOut() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    public static class ConfigurableInputStream extends InputStream {

        private String line;
        private boolean endLine = false;

        @Override
        public int read() throws IOException {
            if (line.length() == 0) {
                return -1;
            }

            if (endLine) {
                endLine = false;
                return -1;
            }

            char ch = line.charAt(0);
            line = line.substring(1);

            if (ch == '\n') {
                endLine = true;
            }

            return (int) ch;
        }

        public void add(String line) {
            if (this.line == null) {
                this.line = line;
            } else {
                this.line += "\n" + line;
            }
        }

        @Override
        public synchronized void reset() throws IOException {
            line = null;
            endLine = false;
        }
    }
}
