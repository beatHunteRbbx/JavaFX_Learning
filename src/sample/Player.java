package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;


public class Player implements Serializable {


    private boolean canJump = true;
    public boolean isMovingRight = false;
    public boolean isMovingLeft = false;
    public static double playerSpeedX = 5;
    public double playerSpeedY = 20;
    public int health = 5;

    public static final int playerWidth = 70;
    public static final int playerHeight = 70;

    private double jumpForce = -20;
    public boolean isHit = false;
    public boolean isDead = false;
    public boolean victory = false;

    private ArrayList<Bullet> bullets = new ArrayList<>();

    public Player (){ }

    public ArrayList<Bullet> getBullets() { return  bullets; }

    public void setCanJump(boolean canJump) { this.canJump = canJump; }

    public void move(double x, double y, ImageView playerView) {

        //движение по ОХ
        boolean movingRight = x > 0;
        for (int i = 0; i < Math.abs(x); i++) {
            for (Box box : Main.OBSTACLES) {
                if (playerView.getBoundsInParent().intersects(box.getBoundsInParent())) {         //проверка на столкновение игрока и препятсвия
                    if (movingRight) {  // проверка столкновения справа от игрока
                        if (playerView.getX() + playerView.getFitWidth() == box.getX()) {     //если произошло столкновение
                            playerView.setX(playerView.getX() - 1);                     //персонаж перемещается на 1 пиксель влево
                            return;
                        }
                    }
                    else {              // проверка столкновения слева от игрока
                        if (playerView.getX() == box.getX() + box.getWidth()) {                         //если произошло столкновение
                            playerView.setX(playerView.getX() + 1);                     //персонаж перемещается на 1 пиксель вправо
                            return;
                        }
                    }
                }
            }
            playerView.setX(playerView.getX() + (movingRight ? 1 : -1));
        }

        //движение по ОY
        boolean movingDown = y > 0;
        for (int i = 0; i < Math.abs(y); i++) {
            for (Box box : Main.OBSTACLES) {
                if (playerView.getBoundsInParent().intersects(box.getBoundsInParent())) {
                    if (movingDown) {   // проверка столкновения снизу от игрока
                        if (playerView.getY() + playerView.getFitHeight() == box.getY()) {    //если произошло столкноение
                            playerView.setY(playerView.getY() - 1);  // сверху          //персонаж перемещается на 1 пиксель вверх
                            playerSpeedY = 1;            //скорость приравнивается к 1, чтобы падение было реалистичным
                            this.setCanJump(true);       //только когда персонаж приземлится на платформу он сможет прыгнуть
                            if (victory) playerSpeedY = -10;  //празднование победы
                            return;
                        }
                    }
                    else {          // проверка столкновения сверху от игрока
                        if (playerView.getY() == box.getY() + box.getHeight()) {                    //если произошло столкновение
                            playerSpeedY = 0;//персонаж перемещается на 1 пиксель вниз
                            return;
                        }
                    }
                }
            }
            //если коллизии не произошло, то персонаж просто падает вниз, а если уже находится на платформе, то поднимается вверх на 1 пиксель
            playerView.setY(playerView.getY() + (movingDown ? 1 : -1));
        }
    }

    public void jump() {
        if (canJump) {
            playerSpeedY = jumpForce;
            canJump = false;
        }
    }
     void fall() {
            //изменение гравитации для Луны
            //на Луне, падение медленней, чем на других уровнях
            if (Main.moonRadioButton.isSelected()) {
                this.playerSpeedY = this.playerSpeedY + 0.5;
            }
            else playerSpeedY++;
     }

    public void shoot(ImageView playerView) {
        Bullet bullet = null;
        if (this.isMovingLeft) {
            bullet = new Bullet(playerView.getX() - 20, playerView.getY() + 20, false, true);
        }
        if (this.isMovingRight) {
            bullet = new Bullet(playerView.getX() + playerView.getFitWidth(), playerView.getY() + 20, true, false);
        }
        Main.playLayout.getChildren().add(1,bullet.getBulletView());
        bullet.getBulletView().setFitWidth(20);
        bullet.getBulletView().setFitHeight(8);
        while (bullets.size() < 1) {
            bullet.getBulletView().setVisible(true);
            bullets.add(bullet);
        }
    }

    public void checkDamage() {
        if (health < 1) isDead = true;
        if(isHit) {
            isHit = false;
            health--;
        }
    }


}
