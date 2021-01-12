/*
    @ Author         : Truong Cong Huy
    @ Class          : CS 311 G
    @ Student Number : 2521 1212 029
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.util.Random;

public class MyGame extends JComponent implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private final int width = 800;
    private final int height = 550;
    private final int widthGiaDo = 200;
    private boolean touched = false;
    private boolean started = false;
    private boolean onButton = false;
    /*
        Mức điểm cần đạt và tốc độ bóng của các độ khó:
            + EASY   ( dễ )         --> 1 bóng chuyển động
            + MEDIUM ( trung bình ) --> 2 bóng chuyển động
            + HARD   ( khó )        --> 3 bóng chuyển động
            + EXPERT ( chuyên gia ) --> 4 bóng chuyển động
    */
    String modeName;
    private final int mediumScore = 3;
    private final int hardScore = 8;
    private final int expertScore = 15;
    private final int superScore = 25;

    private final int speedEasy = 6;
    private final int speedMedium = 7;
    private final int speedHard = 7;
    private final int speedExpert = 9;
    private final int speedSuperBall = 14;

    // Khởi tạo điểm ban đầu, trạng thái game over ?, load ảnh nền game...
    private static int score = 0;
    private static int finalScore = 0;
    private static int highScore = 0;
    private static boolean gameOver = false;
    Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("\\assets\\bg.jpg"));
    
    // Tạo mới các đối tượng bóng và thanh giá đỡ bóng.
    Ball firstBall = new Ball(getRandom(0, width/2), 0, speedEasy, speedEasy);
    Ball secondBall = new Ball(getRandom(5, width), 0, speedMedium, -speedMedium);
    Ball thirdBall = new Ball(getRandom(10, width), 0, -speedHard, speedHard);
    Ball fourthBall = new Ball(getRandom(15, width), 0, speedExpert, speedExpert);
    Ball superBall = new Ball(getRandom(0, width), 0, speedSuperBall, 10);
    GiaDo gd = new GiaDo(0, 500);

    // Phương thức sinh số ngẫu nhiên để chọn toạ độ xuất hiện bóng từ min --> max .
    private static int getRandom(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void main(String[] args){
        // Tiêu đề
        JFrame wind = new JFrame("Project Game Hứng Bóng - SV Trương Công Huy - CS 311 G");
        MyGame game = new MyGame();

        /*
            Add đối tượng MyGame vào trong màn hình JFrame,
            cấu hình thoát chương trình, vị trí xuất hiện
            chương trình ở trung tâm màn hình và hiển thị.
        */
        wind.add(game);
        wind.pack();
        wind.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        wind.setLocationRelativeTo(null);
        wind.setVisible(true);

        /*
            Vô hiệu hoá thay đổi kích thước màn hình,
            lắng nghe sự kiện của chuột, sự kiện
            nhấn phím.
        */
        wind.setResizable(false);
        wind.addMouseMotionListener(game);
        wind.addKeyListener(game);
        wind.addMouseListener(game);


        /*
            Cấu hình FPS của các đối tượng trong game bằng Timer,
            ở đây em mặc định FPS = 60 --> chỉnh delay = 1000/60.
            Với 1 giây = 1000 mili giây --> Mỗi 1000/60 giây
            thì màn hình cập nhật 1 lần ! ( tương tự với các giá trị FPS khác,
            FPS càng cao trải nghiệm càng thực, nhưng cũng cần cấu hình cao )
        */
        int FPS = 60;
        Timer t = new Timer(1000/ FPS, game);
        t.start();

    }

    @Override
    public Dimension getPreferredSize() {

        /*
            Kích thước của màn hình game, ở đây em chọn 800x550
            lệnh wind.setResizable(false); cho nên sẽ không bị thay đổi.
        */
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics game) {

        // Load ảnh background game ở vị trí (0, 0)
        game.drawImage(img, 0, 0, null);

        // Nếu người chơi chưa bắt đầu thì ...
        if (!started){
            // Giữ bóng đứng yên cho đến khi người chơi nhấn ENTER để bắt đầu chơi
            firstBall.setBallxSpeed(0);
            firstBall.setBallySpeed(0);

            // Vẽ viền vàng của intro game
            game.setColor(Color.RED);
            game.fillRect(150, 125, 500, 300);

            // Vẽ phần trắng bên trong intro game
            game.setColor(Color.WHITE);
            game.fillRect(160, 135, 480, 280);

            // Nội dung thông báo
            game.setColor(Color.RED);
            game.setFont(new Font("Arial", Font.BOLD, 50));
            game.drawString("PROJECT GAME", 200, 250);
            game.setFont(new Font("Arial", Font.BOLD, 30));
            game.drawString("SV Trương Công Huy", 250, 295);
            game.setFont(new Font("Arial", Font.BOLD, 30));
            game.drawString("Nhấn ENTER để bắt đầu chơi !", 183, 340);
            game.setFont(new Font("Arial", Font.ITALIC, 20));
            game.drawString("( TIP: Dùng chuột của bạn để điều khiển giá đỡ ) ", 185, 400);

            // Vẽ viền của button "Bắt đầu!"
            game.setColor(Color.WHITE);
            game.fillRect(625, 450, 150, 75);

            // Vẽ phần bên trong button "Bắt đầu!"
            if (onButton){
                game.setColor(Color.YELLOW);
                game.fillRect(629, 454, 142, 67);
                game.setColor(Color.WHITE);
            } else {
                game.setColor(Color.decode("#48e034"));
                game.fillRect(629, 454, 142, 67);
                game.setColor(Color.WHITE);
            }

            game.setFont(new Font("Arial", Font.BOLD, 28));
            game.drawString("Bắt đầu !", 642, 497);
        }
        // started = true --> Khi đã bắt đầu game thì ...
        else {

            // Vẽ vạch đỏ ở đáy
            game.setColor(Color.RED);
            game.fillRect(0, 540, 800, 10);

            // Vẽ giá đỡ bóng với toạ độ x thay đổi và y cố định
            game.setColor(Color.WHITE);
            game.fillRect(gd.getxGiaDo(), gd.getyGiaDo(), widthGiaDo, 25);

            // Tạo một bóng đầu tiên chuyển động
            game.setColor(Color.CYAN);
            game.fillOval(firstBall.getBallx(), firstBall.getBally(), 50, 50);

            /*
                Khi chỉ còn 1 điểm để đạt mức thì thông báo "Một bóng nữa sắp xuất hiện !!!" đến người chơi.
                --------------------------------------------------------------------------------------------
                Khi người chơi ghi được (mediumScore) điểm --> Tăng độ khó lên mức TRUNG BÌNH.
                Tạo ra bóng thứ 2 chuyển động cùng với bóng ban đầu và ẩn thông báo đi.
            */
            if (score == mediumScore - 1) {
                game.setColor(Color.PINK);
                game.setFont(new Font("Arial", Font.BOLD, 30));
                game.drawString("Một bóng nữa sắp xuất hiện !!!", 180, 100);
            }
            if (score >= mediumScore) {
                game.setColor(Color.PINK);
                game.fillOval(secondBall.getBallx(), secondBall.getBally(), 55, 55);
            }

            /*
                Khi người chơi ghi được (hardScore) điểm --> Tăng độ khó lên mức KHÓ.
                Tạo ra bóng thứ 3 chuyển động cùng với 2 bóng kia.
            */
            if (score == hardScore - 1) {
                game.setColor(Color.YELLOW);
                game.setFont(new Font("Arial", Font.BOLD, 30));
                game.drawString("Một bóng nữa sắp xuất hiện !!!", 180, 100);
            }
            if (score >= hardScore) {
                game.setColor(Color.YELLOW);
                game.fillOval(thirdBall.getBallx(), thirdBall.getBally(), 40, 40);
            }

            /*
                Khi người chơi ghi được (expertScore) điểm --> Tăng độ khó lên mức CHUYÊN GIA.
                Tạo ra bóng thứ 4 hơn chuyển động cùng với 3 bóng kia.
            */
            if (score == expertScore - 1) {
                game.setColor(Color.GREEN);
                game.setFont(new Font("Arial", Font.BOLD, 30));
                game.drawString("Một bóng nữa sắp xuất hiện !!!", 180, 100);
            }
            if (score >= expertScore) {
                game.setColor(Color.GREEN);
                game.fillOval(fourthBall.getBallx(), fourthBall.getBally(), 30, 30);
            }

            /*
                Khi đạt (superScore) điểm trở lên sẽ xuất hiện bóng cuối gọi là SUPER BALL
                với tốc độ cực nhanh, nhanh chóng tiễn người chơi tới GAME OVER !!! =))
            */
            if (score == superScore - 1) {
                game.setColor(Color.RED);
                game.setFont(new Font("Arial", Font.BOLD, 30));
                game.drawString("SUPER BALL sắp xuất hiện !!!", 190, 100);
            }
            if (score >= superScore){
                game.setColor(Color.RED);
                game.fillOval(superBall.getBallx(), superBall.getBally(), 60, 60);
            }

            // Cập nhật điểm của người chơi và đổi màu khi đạt mức tại góc màn hình
            if (score >= 10) {
                game.setColor(Color.red);
            } else if (score >= 5) {
                game.setColor(Color.MAGENTA);
            } else
                game.setColor(Color.PINK);

            if (!gameOver) {
                game.setFont(new Font("Arial", Font.BOLD, 25));
                game.drawString("Điểm hiện tại: " + score, 12, 28);
                game.setColor(Color.YELLOW);
                game.drawString("Điểm cao: " + highScore, 12, 56);
            }

            /*
                Cập nhật tên chế độ đang chơi và đổi màu chữ
                ( sẽ thay đổi khi đạt mức điểm yêu cầu )
             */
            if (score >= expertScore) {
                game.setColor(Color.RED);
                modeName = "EXPERT ";
            } else if (score >= hardScore) {
                game.setColor(Color.ORANGE);
                modeName = "HARD ";
            } else if (score >= mediumScore) {
                game.setColor(Color.yellow);
                modeName = "MEDIUM ";
            } else {
                game.setColor(Color.GREEN);
                modeName = "EASY ";
            }
            if (!gameOver) {
                game.setFont(new Font("Arial", Font.BOLD, 20));
                game.drawString("Cấp độ : " + modeName, 625, 25);
            }

            /*
                Nếu GAME OVER thì ẩn bóng và score gán về 0,
                sau đó hiển thị thông báo "BẠN ĐÃ THUA !", điểm của người chơi
                và có thể nhấn phím cách ( SPACE ) để chơi lại !
                Hiển thị button "Chơi lại !" để người chơi click nếu không dùng phím SPACE !
            */
            if (gameOver) {
                firstBall.setBallx(-100);
                firstBall.setBally(-100);
                firstBall.setBallxSpeed(0);
                firstBall.setBallySpeed(0);
                score = 0;

                secondBall.setBallx(getRandom(0, width));
                secondBall.setBally(0);

                thirdBall.setBallx(getRandom(0, width));
                thirdBall.setBally(0);

                fourthBall.setBallx(getRandom(0, width));
                fourthBall.setBally(0);

                superBall.setBallx(getRandom(0, width));
                superBall.setBally(0);

                // Vẽ viền trắng của thông báo
                game.setColor(Color.WHITE);
                game.fillRect(150, 125, 500, 300);

                // Vẽ phần đỏ bên trong thông báo
                game.setColor(Color.RED);
                game.fillRect(160, 135, 480, 280);

                // Nội dung thông báo
                game.setColor(Color.WHITE);
                game.setFont(new Font("Arial", Font.BOLD, 45));
                game.drawString("BẠN ĐÃ THUA !", 240, 250);
                game.setFont(new Font("Arial", Font.BOLD, 35));
                game.drawString("Điểm : " + finalScore, 323, 295);
                game.setFont(new Font("Arial", Font.BOLD, 30));
                game.drawString("Nhấn Space để chơi lại nhé !", 195, 340);
                game.setFont(new Font("Arial", Font.ITALIC, 20));
                game.drawString("Made by Cong Huy with <3", 165, 410);

                // Vẽ viền của button "Chơi lại!"
                game.setColor(Color.WHITE);
                game.fillRect(625, 450, 150, 75);

                // Vẽ phần bên trong button "Chơi lại!"
                if (onButton){
                    game.setColor(Color.YELLOW);
                } else {
                    game.setColor(Color.decode("#48e034"));
                }
                game.fillRect(629, 454, 142, 67);
                game.setColor(Color.WHITE);
                // Viết chữ "Chơi lại !"
                game.setFont(new Font("Arial", Font.BOLD, 28));
                game.drawString("Chơi lại !", 642, 497);
            }
        }
    }


    public void ballMotion(Ball b, int speedX, int speedY){
        /*
            Chuyển động của bóng phụ thuộc vào sụ thay đổi của toạ độ x và y,
            cứ sau mỗi FPS sẽ tăng/giảm đều 1 giá trị vận tốc cho trước.
         */
        b.setBallx( b.getBallx() + b.getBallxSpeed() );
        b.setBally( b.getBally() + b.getBallySpeed() );

        // 2 điều kiện để bóng chạm giá đỡ
        boolean check1 = (b.getBallx() >= gd.getxGiaDo() && b.getBallx() <= gd.getxGiaDo() + widthGiaDo);
        boolean check2 = (b.getBally() >= gd.getyGiaDo() - 30 && b.getBally() <= gd.getyGiaDo());

        /*
            Bóng chạm giá đỡ thì nảy lên hoặc rơi xuống đáy --> Game Over
            Nếu game over thì lưu điểm để pop-up trong thông báo GAME OVER
            Reset điểm về 0, reset bóng, chuyển trạng thái gameOver --> true !
        */
        if (check1 && check2 && !touched){
            b.setBallySpeed(-speedY);
            score++;
            if (score > highScore)
                highScore = score;
            touched = true;
        }

        if (b.getBally() >= height ) {
            finalScore = score;
            if (highScore < finalScore)
                highScore = finalScore;
            score = 0;
            b.setBally(30);
            gameOver = true;
        }

        // Bóng chạm cạnh trên màn hình rồi nảy xuống --> Tăng toạ độ y mỗi FPS
        if (b.getBally() <= 0) {
            b.setBallySpeed(speedY);
            touched = false;
        }

        // Bóng chạm cạnh phải màn hình --> Giảm toạ độ x the mỗi FPS
        if (b.getBallx() >= width - 40) {
            b.setBallxSpeed(-speedX);
            touched = false;
        }

        // Bóng chạm cạnh trái màn hình --> Tăng toạ độ x theo mỗi FPS
        if (b.getBallx() <= 0) {
            b.setBallxSpeed(speedX);
            touched = false;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Bóng đầu tiên bắt đầu chuyển động khi bắt đầu game ( tức là started = true )
        ballMotion(firstBall, speedEasy, speedEasy);

        // Bóng thứ 2 bắt đầu chuyển động khi vào chế độ TRUNG BÌNH
        if (score >= mediumScore) {
            touched = false;
            ballMotion(secondBall, speedMedium, speedMedium);
        }
        // Bóng thứ 3 bắt đầu chuyển động khi vào chế độ KHÓ
        if (score >= hardScore) {
            touched = false;
            ballMotion(thirdBall, speedHard, speedHard);
        }
        // Bóng thứ 4 bắt đầu chuyển động khi vào chế độ CHUYÊN GIA
        if (score >= expertScore) {
            touched = false;
            ballMotion(fourthBall, speedHard, speedHard);
        }
        // SUPER BALL bắt đầu xuất hiện và chuyển động cực nhanh
        if (score >= superScore) {
            touched = false;
            ballMotion(superBall, speedSuperBall, 10);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        /*
            Nếu vị trí chuột trên button "Bắt đầu !" thì onButton = true
            --> Đổi màu button "Bắt đầu !"
            Nếu vị trí chuột trên button "Chơi lại !" thì onButton = true
            --> Đổi màu button "Chơi lại !"
        */
        if (625 < e.getX() && e.getX() < 775 && 450 < e.getY() && e.getY() < 525 && !started){
            onButton = true;
        } else
            onButton = 625 < e.getX() && e.getX() < 775 && 450 < e.getY() && e.getY() < 525 && gameOver;

        /*
           Toạ độ x của giá đỡ bằng toạ độ con trỏ chuột ở lúc đó
           trừ đi 1 nửa chiều rộng giá đỡ để cho con trỏ chuột
           luôn luôn nằm giữa giá đỡ ( với điều kiện chưa GAME OVER ).
        */
        if (!gameOver) {
            gd.setxGiaDo(e.getX() - widthGiaDo / 2);
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        /*
            Nếu người chơi đã bị GAME OVER thì phương thức này có
            chức năng lắng nghe sự kiện người chơi nhấn phím cách ( SPACE )
            hay chưa ? Nếu có thì gán gameOver = false và reset vị trí bóng 1
            cũng như tốc độ rơi (BallxSpeed, BallySpeed) của nó.

            Bonus: Bóng 2, 3 hoặc 4 vì chúng xuất hiệntheo từng cấp độ
            của trò chơi ( expertScore > hardScore > mediumScore > easyScore = 0 )
            và khi GAME OVER thì điểm (score) được gán về 0 !
        */
        int key = e.getKeyCode();

        // Bắt sự kiện nhấn phím ENTER để bắt đầu trò chơi.
        if (key == KeyEvent.VK_ENTER && !started) {
            started = true;
            firstBall.setBallxSpeed(speedEasy);
            firstBall.setBallySpeed(speedEasy);
        }

        // Bắt sự kiện nhấn phím cách ( SPACE ) để chơi lại.
        if (key == KeyEvent.VK_SPACE && gameOver){
            gameOver = false;
            firstBall.setBallx(getRandom(0, width));
            firstBall.setBally(0);
            firstBall.setBallxSpeed(speedEasy);
            firstBall.setBallySpeed(speedEasy);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Khi người chơi click Button "Bắt đầu !" để bắt đầu game.
        if (625 < e.getX() && e.getX() < 775 && 450 < e.getY() && e.getY() < 525 && !started){
            started = true;
            firstBall.setBallxSpeed(speedEasy);
            firstBall.setBallySpeed(speedEasy);
        }

        // Khi người chơi bị GAME OVER và click Button "Chơi lại !" để chơi lại.
        if (625 < e.getX() && e.getX() < 775 && 450 < e.getY() && e.getY() < 525 && gameOver) {
            gameOver = false;
            firstBall.setBallx(getRandom(0, width));
            firstBall.setBally(0);
            firstBall.setBallxSpeed(speedEasy);
            firstBall.setBallySpeed(speedEasy);
        }
    }

    //-----------------------NOTHING-----------------------//
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //----------------------------------------------------//

}
