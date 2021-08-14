#include <iostream>
using namespace std;


class Stack {

    int siz=10;
    int top=0;
    int *st;

public:

    // Overflow constructor function
    Stack() {
        st = new int[10];
    }

    Stack(int n) {
        siz = n;
        st = new int[10];
    }

    Stack(Stack &s) {
        siz = s.siz;
        top = s.top;
        st = new int[siz];
        for (int i=0; i<siz; i++) {
            st[i] = s.st[i];
        }
    }

    // Basic functions
    int pop() {
        if (top == 0) {
            cout << "Stack is empty!" << endl;
            return 0;
        }
        if ( siz - top + 1 > 10)
        // RESIZING
        return st[--top];
    }

    // Push overloading
    void push(int x) {
        if (top == siz)
            // RESIZE
        st[top++]=x;
    }
    void push(int arr[]) {
        for (int i=0; arr[i]!='\0'; i++)
            push(arr[i]);
    }
    void push(Stack &s) {
        for (int i=0; i<s.siz; i++)
            push(s.pop());
    }
    int Top() {
        if (top == 0) {
            cout << "Stack is empty!" << endl;
            return 0;
        }
        else {
            // Returns the element
            // Does not remove it
            return st[top-1];
        }
    }

    int Size() {
        return top;
    }
    double similarity(Stack &s) {
        double average = (double)( (s.top+top)/2 );

        int score=0;
        int size_1 = siz;
        int size_2 = s.siz;
        for (int i=0; i<=size_1 && i<=size_2; i++) {
            if (st[size_1-i] == s.st[size_2-i])
                score++;
        }

        return (double) score/average;
    }

    void Resize() {
        int *temp = new int[top];

        for (int i=0; i<top; i++)
            temp[i] = st[i];

        delete(st);

        st = new int [top+10];

        for (int i=0; i<top; i++)
            st[i] = temp[i];

        delete(temp);
        siz=top+10;
    }

    // Destructor
    ~Stack() {
        delete(st);
    }
};


main() {

    Stack mainstack;
    Stack *tempstack;
    int c, temp, n;
    int *arr;

    while(1) {

        cout << "1: Push an element" << endl;
        cout << "2: Push an array" << endl;
        cout << "3: Push a stack" << endl;
        cout << "4: Pop" << endl;
        cout << "5: Top" << endl;
        cout << "6: Size" << endl;
        cout << "7: Similarity" << endl;
        cout << "8: Exit" << endl;

        cin >> c;

        if(c==1) {
            cin >> temp;
            mainstack.push(temp);
        }
        else if(c==2) {
            cin >> n;
            arr = new int[n];

            for(int i=0; i<n; i++)
                cin >> arr[i];

            mainstack.push(arr);
            delete(arr);
        }
        else if(c==3) {
            cin >> n;

            tempstack = new Stack(n);
            for(int i=0; i<n; i++) {
                cin >> temp;
                tempstack->push(temp);
            }

            mainstack.push(*tempstack);
            delete(tempstack);

        }
        else if(c==4) {
            temp = mainstack.pop();
            cout << "Popped element: " << temp << endl;
        }
        else if(c==5) {
            temp = mainstack.Top();
            cout << "Top element: " << temp << endl;
        }
        else if(c==6) {
            cout << "Size of the stack: " << mainstack.Size() << endl;
        }
        else if(c==7) {
            cin >> n;
            tempstack=new Stack(n);

            for(int i=0;i<n;i++) {
                cin >> temp;
                tempstack->push(temp);
            }

            cout << "Similarity : " << mainstack.similarity(*tempstack) << endl;
            delete(tempstack);
        }
        else if(c==8) {
            n=mainstack.Size();

            cout << "Elements : " << endl;

            for(int i=0; i<n; i++)
            {
                cout << mainstack.pop() << endl;
            }
            break;
        }

    }

    return 0;
}

