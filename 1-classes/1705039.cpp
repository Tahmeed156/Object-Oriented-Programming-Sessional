#include <iostream>
#include <cmath>
#include <sstream>
#include <fstream>
#include <ctime>
#include <cstdlib>

using namespace std;

#define GRIDSIZE 4
#define UP      0
#define RIGHT   1
#define DOWN    2
#define LEFT    3
#define MAX_SHOTS 3


string to_string(int x) {
    std::string out_string;
    std::stringstream ss;
    ss << x;
    return ss.str();
}


class Position {

    int x=0, y=0;

public:

    Position() {
        srand(time(NULL));
        x = rand() % 4;   // range from 0 to 3
        y = rand() % 4;

        if ( x==0 && y==0 ) {
            x += rand()%3 + 1;
            y += rand()%3 + 1;
        }
    }

    Position (int x, int y) {
        this->x = x;
        this->y = y;
    }

    // Modify the following four so that the resulting position does not leave the grid

    void moveRight() {
        if (x < 3)
            x++;
        else
            cout << "Cannot move right" << endl;
    }

    void moveLeft() {
        if (x > 0)
            x--;
        else
            cout << "Cannot move left" << endl;
    }

    void moveUp() {
        if (y < 3)
            y++;
        else
            cout << "Cannot move up" << endl;
    }

    void moveDown() {
        if (y > 0)
            y--;
        else
            cout << "Cannot move down" << endl;
    }

    bool isAdjacent(Position p) {

        //implement the function

        if ( ( ((p.x - x) == 1 || (p.x - x) == -1) && (p.y - y) == 0)
                                    ||
             ( ((p.y - y) == 1 || (p.y - y) == -1) && (p.x - x) == 0) )
            return true;
        else
            return false;
    }

    bool isSamePoint(Position p) {
        //implement the function
        if (p.x == x && p.y == y)
            return true;
        else
            return false;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

};


class Wumpus {

    bool killed;
    Position p;

public:

    Wumpus() {
        killed = false;
    }

    Wumpus(int x, int y) {
        p = Position(x, y);
        killed = false;
    }

    void kill() {
        killed = true;
    }

    Position getPosition() {
        if (!killed)
            return p;
        else
            // Return an unrealistic position
            return Position(4, 4);
    }

};


class Player {

    int direction;
    int total_shots;
    bool killed;
    Position p;

public:

    Player() {
        total_shots = MAX_SHOTS ;
        direction = RIGHT;
        killed=false ;
        p = Position(0, 0) ;
    }

    void turnLeft() {
        if (direction < 1)
            direction = 3;
        else
            direction--;
    }

    void turnRight() {
        if (direction > 2)
            direction = 0;
        else
            direction++;
    }

    void moveForward() {
        if (direction == LEFT)
            p.moveLeft();
        else if (direction == RIGHT)
            p.moveRight();
        else if (direction == UP)
            p.moveUp();
        else if (direction == DOWN)
            p.moveDown();
    }

    bool isAdjacent(Position pos) {
        return p.isAdjacent(pos);
    }

    bool isSamePoint(Position pos) {
        return p.isSamePoint(pos);
    }

    void kill() {
        killed = true;
    }

    int shoot() {
        if ( total_shots>0 )
            return total_shots--;
        else
            return total_shots;
    }

    int getDirection() {
        return direction;
    }

    Position getPosition() {
        return p;
    }

    string getPositionInfo() {
        return "Player is now at: " + to_string(p.getX()) + ", " + to_string(p.getY()) + "\n";
    }

    string getDirectionInfo() {
        string s;
        if (direction == UP) s = "up";
        if (direction == DOWN) s = "down";
        if (direction == LEFT) s = "left";
        if (direction == RIGHT) s = "right";
        return "Player is moving at direction: " + s + "\n";
    }

};



class WumpusWorld {

private:

    Player player;
    Wumpus wumpus;
    Position gold_position;
    Position pit_position;
    bool ended;

public:

    WumpusWorld() {
        ended = false;
    }

    WumpusWorld(int wumpus_x, int wumpus_y) {
        wumpus = Wumpus(wumpus_x, wumpus_y);
        ended = false;
    }

    WumpusWorld(int wumpus_x, int wumpus_y, int gold_x, int gold_y) {
        wumpus = Wumpus(wumpus_x, wumpus_y);
        gold_position = Position(gold_x, gold_y);
        ended = false;
    }

    WumpusWorld(int wumpus_x, int wumpus_y, int gold_x, int gold_y, int pit_x, int pit_y) {
        wumpus = Wumpus(wumpus_x, wumpus_y);
        gold_position = Position(gold_x, gold_y);
        pit_position = Position(pit_x, pit_y);
        ended = false;
    }

    void moveForward() {
        player.moveForward();
        return showGameState();
    }

    void turnLeft() {
        player.turnLeft();
        return showGameState();
    }

    void turnRight() {
        player.turnRight();
        return showGameState();
    }

    void shoot() {

        if (!player.shoot()) {
            cout << "You have no more shots left!" << endl;
            return;
        }

        int kill = 0;
        Position w = wumpus.getPosition();
        Position p = player.getPosition();

        if ( player.getDirection() == RIGHT ) {
            if ( p.getY() == w.getY() && w.getX() > p.getX() )
                kill = 1;
        }
        else if ( player.getDirection()==LEFT ) {
            if ( p.getY() == w.getY() && w.getX() < p.getX() )
                kill = 1;
        }
        else if ( player.getDirection()==UP ) {
            if ( p.getX() == w.getX() && w.getY() > p.getY() )
                kill = 1;
        }
        else if ( player.getDirection()==DOWN ) {
            if ( p.getX() == w.getX() && w.getY() < p.getY() )
                kill = 1;
        }

        if (kill) {
            cout << "You killed the wumpus!" << endl;
            wumpus.kill();
        }
        else {
            cout << "You missed!" << endl;
        }
    }

    void showGameState() {
        cout << player.getPositionInfo();
        cout << player.getDirectionInfo();

        if (player.isAdjacent(wumpus.getPosition())) {
            cout << "stench!" << endl;
        }

        if (player.isSamePoint(wumpus.getPosition())) {
            cout << "Player is killed!" << endl;
            player.kill();
            cout << "Game over!" << endl;
            ended = true;
        }

        // Added two functions to check if you're at and near pit

        if (player.isAdjacent(pit_position)) {
            cout << "breeze!" << endl;
        }

        if (player.isSamePoint(pit_position)) {
            cout << "Player is killed!" << endl;
            player.kill();
            cout << "Game over!" << endl;
            ended = true;
        }

        if (player.isSamePoint(gold_position)) {
            cout << "Got the gold!" << endl;
            cout << "Game ended, you won!" << endl;
            ended = true;
        }
    }

    bool isOver() {
        return ended;
    }

};


int main()
{
    int c, wumpus_x, wumpus_y, gold_x, gold_y, pit_x, pit_y;

    int x[3] = {2, 3, 4};
    ifstream f;

    f.open("info.txt", ios::in);

    f >> wumpus_x >> wumpus_y;
    f >> gold_x >> gold_y;
    f >> pit_x >> pit_y;

    f.close();

    // take the six integers input from file
    WumpusWorld w(wumpus_x, wumpus_y, gold_x, gold_y, pit_x, pit_y);
    w.showGameState();

    while (!w.isOver()) {
        cout << "1: move forward" << endl;
        cout << "2: Turn left" << endl;
        cout << "3: Turn right" << endl;
        cout << "4: Shoot" << endl;
        cin >> c;
        if (c == 1) {
            w.moveForward();
        } else if (c == 2) {
            w.turnLeft();
        } else if (c == 3) {
            w.turnRight();
        } else if (c == 4) {
            w.shoot();
        }
    }

    return 0;
}
