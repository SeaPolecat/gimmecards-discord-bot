package ca.gimmecards.utils;
import java.util.List;

public class SearchUtils {
    
    public static <T extends Comparable<T>> int binarySearch(List<T> list, T targetItem) {
        int beg = 0, end = list.size() - 1;

        while(beg <= end) {
            int mid = (beg + end) / 2;
            T midItem = list.get(mid);

            if(midItem.compareTo(targetItem) == 0)
                return mid;

            else if(midItem.compareTo(targetItem) < 0)
                beg = mid + 1;

            else
                end = mid - 1;
        }
        return beg;
    } 
}