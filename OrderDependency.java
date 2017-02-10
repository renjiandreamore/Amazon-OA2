/*Amazon-OA2 Order Dependency(course schedule ii)

这道题的坑就是，多个Order会对应同一个order name（String），那么在排序的时候这多个order只出现一个。
题目大概内容是这样的，。输入的是一群OrderDependency的object，每个OrderDependency里面装着两个Order，
就是一个指向另一个，输出是Order的List。比如想去SF，就要先租车，想租车，就要有钱，想有钱，就得吃半个月的泡面。
所以输出顺序就是先吃半个月泡面，然后租车，再去SF，一环扣一环。还有SF是三番的意思绝对不是少妇啊不要想歪了。
本质上就是Leetcode 210题，思路做法是一样的，不过要多做一点预处理。

解决思路

首先用脚后跟想想，这题必须是topological sorting。我个人非常偏向使用BFS，剥洋葱法。
那么就按照这个思路来想，每个order都用map记录入度，还有个map记录这个order指向多少个order。
另外再用一个set来记录出现过的order，用来判断最后是否有环（据说不会成环，但我这人有完美主义必须加以判断）。
还有就是因为最后输出是List<Order>，但是排序的时候都是字符串来排序，所以要加上个map，做成orderName，Order的对儿，
在最后根据排好的string把Order拎出来。

*/

import java.util.*;//后来亚马逊自己加上了这一行
class Order{
    String orderName;
    public Order(String string){
        this.orderName = string;
    }
}
class OrderDependency{
    Order cur;
    Order pre;
    public OrderDependency(Order pre, Order cur){
        this.pre = pre;
        this.cur = cur;
    }
}

public class Solution {
    public static List<Order> solution(List<OrderDependency> orderDependencies){
        //拿来输出
        List<Order> result = new ArrayList<>();
        //接下来所有的key都保存的是string,orderName,这样直接避免重复输出。
        //这个是计算入度
        Map<String, Integer> inMap = new HashMap<>();
        //出度,就是一个Order指着多少个其他Order
        Map<String, List<String>> outMap = new HashMap<>();
        //这个负责记录,记录orderName和Order的关系
        Map<String, Order> recordMap = new HashMap<>();
        //这个负责去掉重复
        Set<String> set = new HashSet<>();

        //下面开始把硅胶填入到这些map或是set里面,使其丰满
        //这里就注意要仔细就行,别漏东西
        for (OrderDependency od : orderDependencies) {
            Order pre = od.pre;
            Order cur = od.cur;
            String preName = pre.orderName;
            String curName = cur.orderName;
            set.add(preName);
            set.add(curName);
            //先塞好string和Order的map
            if (!recordMap.containsKey(preName)) {
                recordMap.put(preName, pre);
            }
            if (!recordMap.containsKey(curName)) {
                recordMap.put(curName, cur);
            }

            //先做好入度
            if (!inMap.containsKey(preName)) {
                inMap.put(preName, 0);
            }
            if (inMap.containsKey(curName)) {
                inMap.put(curName, inMap.get(curName) + 1);
            }
            else {
                inMap.put(curName, 1);
            }
            //再做一下出度
            List<String> temp = new ArrayList<>();
            if (outMap.containsKey(preName)) {
                temp = outMap.get(preName);
            }
            temp.add(curName);
            outMap.put(preName, temp);
            //这里得加个空表,否则后面的null会报错的
            if (!outMap.containsKey(curName)) {
                outMap.put(curName, new ArrayList<>());
            }
        }

        //一路BFS剥洋葱,每层都挑入度为0的string
        Queue<String> queue = new LinkedList<>();
        for(String str : inMap.keySet()){
            int inDegree = inMap.get(str);
            if (inDegree == 0) {
                queue.offer(str);
            }
        }
        while(!queue.isEmpty()){
            String top = queue.poll();
            //这里发挥了recordMap的作用
            result.add(recordMap.get(top));
            //这里挑出来之后,找它下家入度为0的
            List<String> outList = outMap.get(top);
            for (String next : outList) {
                inMap.put(next, inMap.get(next) - 1);
                if (inMap.get(next) == 0) {
                    queue.offer(next);
                }
            }
        }
        //这一步判断成环估计不需要吧,以防万一
        if (set.size() != result.size()){
            return null;
        }
        return result;
    }
    //这里是测试。
    public static void main(String[] args) {
        Order o1 = new Order("泡面");
        Order o2 = new Order("泡面");
        Order o3 = new Order("SF");
        Order o4 = new Order("租车");
        Order o5 = new Order("SF");
        Order o6 = new Order("泡面");
        Order o7 = new Order("租车");
        Order o8 = new Order("SF");
        Order o9 = new Order("爽(每个行为只输出了一次对吧)");
        OrderDependency od1 = new OrderDependency(o1, o3);
        OrderDependency od2 = new OrderDependency(o2, o7);
        OrderDependency od3 = new OrderDependency(o3, o9);
        OrderDependency od4 = new OrderDependency(o4, o3);
        OrderDependency od5 = new OrderDependency(o6, o9);
        OrderDependency od6 = new OrderDependency(o8, o9);
        OrderDependency od7 = new OrderDependency(o2, o5);

        List<OrderDependency> list = new ArrayList<>();
        list.add(od1);
        list.add(od2);
        list.add(od3);
        list.add(od4);
        list.add(od5);
        list.add(od6);
        list.add(od7);
        //最后输出就是这种形式
        List<Order> res = solution(list);
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i).orderName);
            if (i+1 < res.size()){
                System.out.print(" -> ");
            }
        }
    }
}


//O(n)
