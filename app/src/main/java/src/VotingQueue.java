package src;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by spg01 on 11/5/2017.
 */

public class VotingQueue<T> {

    PriorityQueue<VotingQueueItem> queue;

    public VotingQueue() {
        queue = new PriorityQueue<VotingQueueItem>() {

            public Comparator<VotingQueueItem> comparator() {
                return new Comparator<VotingQueueItem>() {
                    @Override
                    public int compare(VotingQueueItem o1, VotingQueueItem o2) {
                        return o1.priority - o2.priority;
                    }
                };
            }

        };

    }

    public T[] getList() {
        ArrayList<T> myList = new ArrayList<>();

        return (T[]) myList.toArray();
    }

    public void push(T item) {
        queue.add(new VotingQueueItem(item));
    }

    public void upvote(int index) {

    }

    private class VotingQueueItem {

        private T item;
        private int priority;

        VotingQueueItem(T item) {
            this.item = item;
            this.priority = 1;
        }

        VotingQueueItem(T item, int index) {
            this.item = item;
            this.priority = index;
        }

        void upvote() {
            this.priority++;
        }

        void downvote() {
            if (this.priority > 0)
                this.priority--;
        }

    }

}
