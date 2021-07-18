package com.Project;

import processing.core.PApplet;

public class SnakeGame extends PApplet {
    public static void main(String[] args) {
        PApplet.main("com.Project.SnakeGame", args);
    }


    final int playSize = 600;
    final int displayAreaWidth = 200;
    int boxSize = 40;
    int res = playSize / boxSize;
    Snake snake;
    int direction = (int) random(4);
    Snake.Part food;

    public void setup() {
        frameRate(3);
    }

    public void settings() {
        size(playSize + displayAreaWidth, playSize);
        snake = new Snake(res / 2, res / 2);
        newFood();
    }

    public void draw() {
        frameRate(map(snake.len, 1, res*res, (float) (res*0.005+3), 6));
        background(0);
        strokeWeight(2);
        stroke(255);
        for (int i = 0; i < res; i++) {
            line((i + 1) * boxSize, 0, (i + 1) * boxSize, playSize);
            line(0, (i + 1) * boxSize, playSize, (i + 1) * boxSize);
        }
        if (atSamePos(snake.head, food)) {
            snake.expand();
            newFood();
        }
        if (snake.check()) {
            snake.move(direction);
            food.show();
            snake.show();
            fill(255);
            textAlign(CENTER, CENTER);
            textSize(30);
            text("Score:", playSize + (displayAreaWidth / 2), playSize / 2 - 20);
            text(snake.len, playSize + (displayAreaWidth / 2), playSize / 2 + 20);
        } else {
            snake = new Snake(res / 2, res / 2);
            direction = (int) random(4);
            newFood();
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP && direction != 3) {
                direction = 1;
            } else if (keyCode == DOWN && direction != 1) {
                direction = 3;
            } else if (keyCode == RIGHT && direction != 2) {
                direction = 0;
            } else if (keyCode == LEFT && direction != 0) {
                direction = 2;
            }
        }
    }

    void newFood() {
        food = snake.new Part((int) random(res), (int) random(res));
        while (true) {
            boolean goodOne = true;
            for (int i = 0; i < snake.len; i++) {
                if (atSamePos(snake.parts[i], food))
                    goodOne = false;
            }
            if (goodOne)
                break;
            food = snake.new Part((int) random(res), (int) random(res));
        }
        food.partColor = color(20, 220, 40);
    }

    class Snake {
        Part[] parts;
        int len;
        int snakeColor = color(200, 0, 150);
        Part head;

        Snake(int x, int y) {
            parts = new Part[1];
            parts[0] = new Part(x, y);
            head = parts[0];
            head.partColor = snakeColor;
            len = 1;
        }

        void show() {
            for (int i = 0; i < len; i++) {
                parts[i].show();
            }
        }

        public boolean check() {
            Part forward = new Part(head.x, head.y);
            if (direction == 1) {
                forward.moveUp();
            } else if (direction == 3) {
                forward.moveDown();
            } else if (direction == 0) {
                forward.moveRight();
            } else if (direction == 2) {
                forward.moveLeft();
            }
            if (forward.x < 0 || forward.y < 0)
                return false;
            if (forward.x >= res || forward.y >= res)
                return false;
            for (int i = 0; i < len; i++)
                if (atSamePos(parts[i], forward))
                    return false;
            return true;
        }

        void move(int direction) {
            Part[] lastParts = new Part[len];
            for (int i = 0; i < len; i++) {
                lastParts[i] = new Part(parts[i].x, parts[i].y);
                lastParts[i].partColor = parts[i].partColor;
            }
            if (direction == 0) {
                head.moveRight();
            } else if (direction == 1) {
                head.moveUp();
            } else if (direction == 2) {
                head.moveLeft();
            } else if (direction == 3) {
                head.moveDown();
            }
            for (int i = 1; i < len; i++) {
                parts[i] = lastParts[i - 1];
            }
        }

        void expand() {
            Part newPart = new Part(parts[len - 1].x, parts[len - 1].y);
            newPart.partColor = snakeColor;
            len++;
            parts = (Part[]) append(parts, newPart);
        }

        void snakePrint() {
            println("Snake:");
            for (int i = 0; i < len; i++) {
                parts[i].printPart();
            }
        }

        class Part {
            int partColor;
            int x, y;

            Part(int x_, int y_) {
                x = x_;
                y = y_;
            }

            void show() {
                noStroke();
                fill(partColor);
                rect(x * boxSize +1, y * boxSize+1 , boxSize-2, boxSize -2);
            }

            void moveRight() {
                x++;
            }

            void moveLeft() {
                x--;
            }

            void moveUp() {
                y--;
            }

            void moveDown() {
                y++;
            }

            void printPart() {
                println("x = " + x + ", y = " + y);
            }
        }
    }

    boolean atSamePos(Snake.Part a, Snake.Part b) {
        return (a.x == b.x && a.y == b.y);
    }
}
