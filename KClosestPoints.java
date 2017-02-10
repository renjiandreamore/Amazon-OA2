
/**
  Given points = [[4,6],[4,7],[4,4],[2,5],[1,1]], origin = [0, 0], k = 3
  return [[1,1],[2,5],[4,4]]
  
 * Definition for a point.
 * class Point {
 *     int x;
 *     int y;
 *     Point() { x = 0; y = 0; }
 *     Point(int a, int b) { x = a; y = b; }
 * }
 */
public class Solution {
    /**
     * @param points a list of points
     * @param origin a point
     * @param k an integer
     * @return the k closest points
     */
    public Point global_origin = null;
    public Point[] kClosest(Point[] points, Point origin, int k) {
        // Write your code here
        //1. 全局变量
        //2. 注意k>size的情况！ if so， 新len应该是points的len不是k
        if(k <= 0 || points == null || points.length == 0) {
            return new Point[0];
        }
        global_origin = origin;
        
        PriorityQueue<Point> pq = new PriorityQueue<Point>(k, new pqCmp());
        for(Point p : points) {
            pq.offer(p);
        }
        
        int len = k >= points.length ? points.length : k;
        Point[] res = new Point[len];
        
        int index = 0;
        while(!pq.isEmpty() && index < len) {
            res[index++] = pq.poll();
        }
        
        return res;
        
    }
    
    class pqCmp implements Comparator<Point>{
        @Override
        public int compare(Point a, Point b) {
            int distance_a = (a.x - global_origin.x) * (a.x - global_origin.x) + (a.y - global_origin.y) * (a.y - global_origin.y);
            int distance_b = (b.x - global_origin.x) * (b.x - global_origin.x) + (b.y - global_origin.y) * (b.y - global_origin.y);
            
            if(distance_a == distance_b) {
                if(a.x != b.x){
                    return b.x - a.x;
                }
                else{
                    return b.y - a.y;
                }
            }
            else{
                return distance_a - distance_b;
            }
        }
    }
}
