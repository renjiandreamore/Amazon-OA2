/**
 * Definition for a Record
 * class Record {
 *     public int id, score;
 *     public Record(int id, int score){
 *         this.id = id;
 *         this.score = score;
 *     }
 * }
 
 Given results = [[1,91],[1,92],[2,93],[2,99],[2,98],[2,97],[1,60],[1,58],[2,100],[1,61]]
 */
public class Solution {
    /**
     * @param results a list of <student_id, score>
     * @return find the average of 5 highest scores for each person
     * Map<Integer, Double> (student_id, average_score)
     */
    public Map<Integer, Double> highFive(Record[] results) {
        // Write your code 
        //每人至少有5份成绩，要取最高的5份的average
        if(results == null || results.length < 5) return new HashMap<Integer, Double>();
        
        PriorityQueue<Record> pq = new PriorityQueue<Record>(results.length, new PqCmp());
        for(Record re : results) {
            pq.offer(re);
        }
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        
        while(!pq.isEmpty()) {
            Record re = pq.poll();
            if(!map.containsKey(re.id)) {
                map.put(re.id, new ArrayList<Integer>());
                map.get(re.id).add(re.score);
            }else{
                if(map.get(re.id).size() < 5) {
                    map.get(re.id).add(re.score);
                }
            }
        }
        
        Map<Integer, Double> res = new HashMap<>();
        for(Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            int scores = 0;
            for(int score : entry.getValue()) {
                scores += score;
            }
            Double s = scores / 5.0;
            res.put(entry.getKey(), s);
        }
        
        return res;
    }
    
    class PqCmp implements Comparator<Record>{
        @Override
        public int compare(Record R1, Record R2) {
            return R2.score - R1.score;
        }
    }
}

