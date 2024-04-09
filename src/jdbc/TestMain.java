package jdbc;

public class TestMain {
    public static void main(String[] args) {
        JdbcModel model = new JdbcModel();
        // 회원정보 전체 출력
        model.empSelectAll();
    }
}
