package com.company;

class LinkedList {
    private IntPair intPair;
    private LinkedList next;

    LinkedList(IntPair intPair, LinkedList next) {
        this.intPair = intPair;
        this.next = next;
    }

    LinkedList(LinkedList l) {
        this.intPair = new IntPair(l.getIntPair().x, l.getIntPair().y);
        this.next = null;
    }

    IntPair getIntPair() {
        return intPair;
    }

    LinkedList getNext() {
        return next;
    }

    void setNext(LinkedList next) {
        this.next = next;
    }
}
