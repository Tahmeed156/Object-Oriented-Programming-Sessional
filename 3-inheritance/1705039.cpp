#include <iostream>
using namespace std;

#define TOTAL_SIZE 100

class Unit {
protected:
    char * unit_name;
    int range;
    int health;
    int damage;
    int cost;
    int position;

    int step_size;
    int return_coin;

    char * shoot_type;
    char * movement;

public:

    Unit () {
        position = 0;
    }
    Unit (int n) {
        position = n;
    }
    Unit (char *un, int r, int h, int d, int c, int ss, int rc,
          char * st, char * m) {
        unit_name = un;
        range = r;
        health = h;
        damage = d;
        cost = c;
        position = 0;

        step_size = ss;
        return_coin = rc;
        movement = m;
        shoot_type = st;
    }
    int unit_cost () {
        return cost;
    }
    void deplete_health(int n) {
        health -= n;
    }
    bool is_alive(int & r) {
        if (health > 0) {
            return true;
        }
        else {
            cout << endl << unit_name << " was killed!" << endl << endl;
            r = return_coin;
            return false;
        }
    }
    int damage_rate() {
        return damage;
    }
    bool in_range() {
        return range >= (TOTAL_SIZE - position);
    }

    // Overloaded operators
    int operator - (Unit obj) {
        // Depletes health
        health = health = obj.damage_rate();
        return health;
    }
    int move_forward () {
        // Moves forward
        if ( position + step_size > 100  ||
             movement == "N/A")
            return position;
        position = position + step_size;
        return position;
    }
    void unit_info() {
        cout << endl;
        cout << "Unit name : " << unit_name << endl;
        cout << "Range : " << range << endl;
        cout << "Health : " << health << endl;
        cout << "Position : " << position << endl;
        cout << "Damage : " << damage << endl;
        cout << "Cost : " << cost << endl;
        cout << "Step Size : " << step_size << endl;
        cout << "Return Coin : " << return_coin << endl;
        cout << "Shoot Type : " << shoot_type << endl;
        cout << "Movement : " << movement << endl;
        cout << "In range : " << in_range() << endl;
        cout << endl;
    }
    virtual void unit_status() {
        cout << unit_name << " | ";
        cout << "Health : " << health << " | ";
        cout << "Position : " << position << " | ";
        cout << "Movement : " << movement << endl;
        if (in_range()) {
            cout << unit_name << " shot " <<
            shoot_type;
        } else {
            cout << unit_name << " is not in range to shoot";
        }
        cout  << endl;
    }
};

class Archer : public Unit {
public:
    Archer(int n): Unit(n) {
        unit_name = "Archer";
        range = 50;
        health = 100;
        damage = 25;
        cost = 150;

        step_size = 25;
        return_coin = 0;
        shoot_type = "advanced arrow";
        movement = "running";
    }
};

class AdvancedArcher : public Unit {
public:
    AdvancedArcher(int n): Unit(n) {
        unit_name = "Advanced Archer";
        range = 50;
        health = 120;
        damage = 50;
        cost = 600;

        step_size = 30;
        return_coin = 0;
        shoot_type = "improved arrow";
        movement = "riding horse";
    }
};

class Bowman : public Unit {
public:
    Bowman(int n): Unit(n) {
        unit_name = "Bowman";
        range = 110;
        health = 60;
        damage = 10;
        cost = 100;

        step_size = 0;
        return_coin = 40;
        shoot_type = "basic arrow";
        movement = "N/A";
    }
};

class AdvancedBowman : public Unit {
public:
    AdvancedBowman(int n): Unit(n) {
        unit_name = "Advanced Bowman";
        range = 130;
        health = 85;
        damage = 15;
        cost = 200;

        step_size = 0;
        return_coin = 60;
        shoot_type = "cannon";
        movement = "N/A";
    }
};

class EnemyTower : public Unit {
public:
    EnemyTower(int n): Unit(n) {
        unit_name = "Enemy Tower";
        range = 200;
        health = 300;
        damage = 40;
        cost = 1000;
        position = n;

        step_size = 0;
        return_coin = 0;
        shoot_type = "fire arrow";
        movement = "N/A";
    }
    void unit_status() {
        cout << "Enemy tower's health : " << health << endl;
        cout << unit_name << " is shooting " << shoot_type;
        cout  << endl;
    }
};

void display_choices(int coins) {
    cout << "Coins remaining : " << coins << endl << endl;
    cout << "Choose your option :" << endl;
    cout << "1. Archer (Cost 150)" << endl;
    cout << "2. Advanced Archer (Cost 600)" << endl;
    cout << "3. Bowman (Cost 100)" << endl;
    cout << "4. Advanced Bowman (Cost 200)" << endl;
}

int main()
{
    Unit *w;
    Unit *e = new EnemyTower(TOTAL_SIZE);
    int choice;
    int coins = 1600, return_coin = 0;
    int round = 0;

    while(coins > 0)
    {
        display_choices(coins);
        cin >> choice;

        switch(choice)
        {
            case 1: w =  new Archer(0);
                    break;
            case 2: w = new AdvancedArcher(0);
                    break;
            case 3: w =  new Bowman(0);
                    break;
            default : w = new AdvancedBowman(0);
                    break;
        }

        if ( w->unit_cost() > coins ) {
            cout << "You don't have enough coins!" << endl;
            continue;
        }
        else {
            coins -= w->unit_cost();
        }
        // w->unit_info();

        while (w->is_alive(return_coin)) {
            cout << "Round : " << round+1 << endl;
            // Increments position
            w->move_forward();
            // Shows status
            w->unit_status();
            // Check if in range & then attack
            if ( w->in_range() ) {
                e->deplete_health(w->damage_rate());
            }
            e->unit_status();
            // Enemy tower attacks
            w->deplete_health(e->damage_rate());

            round++;
        }

        coins += return_coin;

        if ( !(e->is_alive(return_coin)) ) {
            cout << "Congratulations! You won the game!" << endl;
            return 0;
        }
        return_coin = 0;

    }
}
