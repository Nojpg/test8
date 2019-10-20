import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();

        int[] cabin = new int[N];
        int used= 0;
        int sum = 0;
        LList lList = new LList();

        for (int i = 0; i < M; i++) {
            int status = scanner.nextInt();
            int id = scanner.nextInt();

            if (used == 0 && status == 1) {
                cabin[0] = id;
                used++;
                sum ++;
            } else if (used == 1 && status == 1) {
                cabin[N-1] = id;
                sum += N;
                used ++;
                lList.insert(new Range(1, N-2));
            } else if (lList.size() != 0 && status == 1) {
                int pos = (lList.head.data.start + lList.head.data.end) / 2;
                Range range1 = new Range(lList.head.data.start, pos -1);
                Range range2 = new Range(pos - 1, lList.head.data.end);
                lList.remove(lList.head.data);
                lList.insert(range1);
                lList.insert(range2);
                sum += pos;
                cabin[pos] = id;
            } else if (status == 2){
                int j = 0;
                while (cabin[j] != id) {
                    j++;
                }
                cabin[j] = 0;
                Range range = new Range(j, j);
                lList.reBalanceWith(range);
            }
        }

        System.out.println(sum);
    }

    static class Range{
        int start;
        int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Range)) return false;

            Range range = (Range) o;

            if (start != range.start) return false;
            return end == range.end;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }
    }

    static class LList {
        Node head;

        private class Node {
            Node next;
            Range data;

            public Node(Range data) {
                this.data = data;
                this.next = null;
            }
        }

        void insert(Range range) {
            if (this.head == null) {
                this.head = new Node(range);
            } else {
                Node last = this.head;
                while (last.next != null) {
                    last = last.next;
                }
                last.next = new Node(range);
            }
        }

        int size() {
            int size = 0;
            Node last = this.head;
            while(last != null) {
                size++;
                last = last.next;
            }
            return size;
        }

        void remove(Range range) {
            Range data = this.head.data;
            Node node = this.head;
            if (data == range) {
                this.head = null;
            } else {
                while (data != range) {
                    node = this.head.next;
                }
                node.next = null;
            }

        }

        Range find(Range range) {
            Node head = this.head;
            if (head == null) {
                return null;
            }
            Range data = head.data;
            while (data.end > range.end && data != null) {
                head = head.next;
                data = head.data;
            }
            if (data.end <= range.start || data.start >= range.end) {
                return data;
            } else {
                return null;
            }
        }

        void reBalanceWith(Range range) {
            Range data = find(range);
            if (data == null) {
                return;
            }
            if (data.start - 1 == range.end) {
                data.start = range.start;
            } else if (data.end + 1 == range.start) {
                data.end = range.end;
            }
            Range tmp = find(data);
            if (tmp != null) {
                reBalanceWith(tmp);
            }
        }
    }
}
