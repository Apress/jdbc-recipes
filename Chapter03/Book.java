  import java.sql.SQLData;
  import java.sql.SQLInput;
  import java.sql.SQLOutput;
  import java.sql.SQLException;
  import java.io.Serializable;

    /**
     * A class to hold a copy of "SCOTT.BOOK" data type
     *
     *
     *
     * @author Mahmoud Parsian
     * @email  admin@jdbccookbook.com
     *
     */
     public class Book implements SQLData, Serializable {

        public static final String SQL_TYPE_NAME = "SCOTT.BOOK";
        public String isbn;
        public String title;
        public String author;
        public int edition;

    // this constructor is required by Oracle's JDBC driver.
    // if you exclude this constructor, then you will get a
    // SQLException: "Inconsistent java and sql object types:
    // InstantiationException: Book"
    public Book() {
    }

    public Book (String isbn,
                 String title,
                 String author,
                 int edition) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.edition = edition;
    }

    // retrieves the fully qualified name of the SQL
    // user-defined type that this object represents.
    public String getSQLTypeName() {
        return SQL_TYPE_NAME;
    }

    // populates this object with data it reads from stream
    public void readSQL(SQLInput stream, String sqlType)
        throws SQLException {
        this.isbn = stream.readString();
        this.title = stream.readString();
        this.author = stream.readString();
        this.edition = stream.readInt();
    }

    // writes this object to stream
    public void writeSQL(SQLOutput stream)
        throws SQLException {
        stream.writeString(this.isbn);
        stream.writeString(this.title);
        stream.writeString(this.author);
        stream.writeInt(this.edition);
    }

    /**
     * For debugging: prints the raw data obtained from db.
     */
    public void print() {
        System.out.println("--- Book print() raw data begin ---");
        System.out.println("isbn="+isbn);
        System.out.println("title="+title);
        System.out.println("author="+author);
        System.out.println("edition="+edition);
        System.out.println("--- Book print() raw data end ---");
    }
}