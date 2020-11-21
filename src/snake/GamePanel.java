package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * 游戏面板
 */
public class GamePanel extends JPanel implements KeyListener, ActionListener {

    //定义蛇的数据结构
    int length;  //蛇的长度
    int[] snakeX = new int[600];   //蛇的X坐标
    int[] snakeY = new int[500];   //蛇的Y坐标
    String toward;

    //食物的坐标
    int foodx;
    int foody;
    Random random = new Random();

    //成绩
    int score;

    //游戏当前的状态
    boolean isStart = false;   //默认是不开始

    boolean isFail = false;  //游戏失败状态

    //定时器
    Timer timer = new Timer(100,this);   //100毫秒执行一次

    //构造器
    public GamePanel(){
        init();
        //获得焦点和键盘事件
        this.setFocusable(true);  //获得焦点事件
        this.addKeyListener(this);   //获得键盘监听事件
        timer.start();  //游戏一打开定时器就启动
    }

    public void init(){
        length = 3;
        snakeX[0] = 100;  snakeY[0] = 100;
        snakeX[1] = 75;  snakeY[1] = 100;
        snakeX[2] = 50;  snakeY[2] = 100;
        toward = "R";

        //把食物随机分布在界面上
        foodx = 25 + 25 * random.nextInt(34);
        foody = 75 + 25 * random.nextInt(24);

        score = 0;
    }

    //绘制面板，所有的东西都用这个画笔来画
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//清屏
        //绘制静态的面板
        this.setBackground(Color.WHITE);
        Data.header.paintIcon(this, g, 25, 11);  //头部广告栏
        g.fillRect(25, 75, 850, 600);  //默认的游戏界面

        //画积分
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g.drawString("长度" + length, 750, 35);
        g.drawString("分数" + score, 750, 50);

        //画食物
        Data.food.paintIcon(this,g,foodx,foody);

        //把小蛇画上去
        if(toward.equals("R")){
            Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);  //蛇头初始化向右
        }else if(toward.equals("L")){
            Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);  //蛇头初始化向左
        } else if (toward.equals("U")) {
            Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);  //蛇头初始化向上
        } else if (toward.equals("D")) {
            Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);  //蛇头初始化向下
        }


        for(int i=1; i<length; i++){
            Data.body.paintIcon(this, g, snakeX[i], snakeY[i]);  //第一个身体的坐标
        }

        //游戏状态
        if(isStart==false){
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("按下空格开始游戏", 300, 300);
        }

        if(isFail){
            g.setColor(Color.RED);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("失败，按下空格重新开始游戏", 300, 300);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    //键盘监听事件
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();  //获得键盘按的是哪个键

        if (keyCode == KeyEvent.VK_SPACE) {
            if(isFail){
                //重新开始
                isFail =false;
                init();
            }else {
                isStart = !isStart;
            }
            repaint();
        }
        //小蛇移动
        if(keyCode == KeyEvent.VK_UP){
            toward = "U";
        } else if (keyCode == KeyEvent.VK_DOWN) {
            toward = "D";
        } else if (keyCode == KeyEvent.VK_LEFT) {
            toward = "L";
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            toward = "R";
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    //事件监听
    @Override
    public void actionPerformed(ActionEvent e) {
        if(isStart && isFail == false){ //如果游戏是开始状态，就让小蛇动起来

            //吃食物
            if(snakeX[0] == foodx && snakeY[0] == foody){
                length++;   //长度+1
                score += 10;
                //再次随机食物
                foodx = 25 + 25 * random.nextInt(34);
                foody = 75 + 25 * random.nextInt(24);
            }

            //移动
            for(int i = length-1; i>0; i--){
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }

            //走向
            if(toward.equals("R")){
                snakeX[0] = snakeX[0] + 25;
                //边界判断
                if(snakeX[0]>850){
                    snakeX[0] = 25;
                }
            } else if (toward.equals("L")) {
                snakeX[0] = snakeX[0] - 25;
                //边界判断
                if(snakeX[0]<25){
                    snakeX[0] = 850;
                }
            } else if (toward.equals("U")) {
                snakeY[0] = snakeY[0] - 25;
                //边界判断
                if(snakeY[0]<75){
                    snakeY[0] = 650;
                }
            } else if (toward.equals("D")) {
                snakeY[0] = snakeY[0] + 25;
                //边界判断
                if(snakeY[0]>650){
                    snakeY[0] = 75;
                }
            }

            //失败判断，撞到自己就算失败
            for(int i=1; i<length; i++){
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                    isFail = true;
                }
            }

            repaint();
        }
        timer.start();
    }
}

