package utils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ReportUtil {

	public static void generateReport(HashMap<String, HashMap<String, String>> dataFromExcel,
			HashMap<String, HashMap<String, String>> dataFromUI) throws ParseException {
		HashMap<String, HashMap<String, String>> sameKeys = findCommonKeys(dataFromExcel, dataFromUI);

		for (Entry<String, HashMap<String, String>> entry : sameKeys.entrySet()) {
			HashMap<String, HashMap<String, String>> map = new HashMap<>();
			map.put(entry.getKey(), entry.getValue());
			ExcelUtil.writeDatainExcel(map,
					String.valueOf(compareMaps(dataFromExcel.get(entry.getKey()), dataFromUI.get(entry.getKey()))));
		}

		HashMap<String, HashMap<String, String>> differentKeys = getDifferentKeys(dataFromExcel, dataFromUI);

		ExcelUtil.writeDatainExcel(differentKeys, "Data not on UI");
		
	}

	private static <K, V> HashMap<K, V> getDifferentKeys(Map<K, V> map1, Map<K, V> map2) {
		Set<K> keys1 = map1.keySet();
		Set<K> keys2 = map2.keySet();

		return (HashMap<K, V>) keys1.stream().filter(key -> !keys2.contains(key))
				.collect(Collectors.toMap(key -> key, map1::get));

	}

	private static <K, V> HashMap<K, V> findCommonKeys(Map<K, V> map1, Map<K, V> map2) {
		return (HashMap<K, V>) map1.entrySet().stream().filter(entry -> map2.containsKey(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static boolean compareMaps(HashMap<String, String> map1, HashMap<String, String> map2) {
		map1.remove("MeterNo");
		map1.remove("Account ID");
		map1.remove("Time");
		map1.remove("Date");
		return map1.equals(map2);
	}
}
