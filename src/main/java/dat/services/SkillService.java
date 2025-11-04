// dat/services/SkillStatsService.java
package dat.services;

import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

public class SkillService {
    private final FetchTools fetchTools;

    public SkillService(FetchTools fetchTools) { this.fetchTools = fetchTools; }

    //Henter markedstal for flere slugs i ét kald. Returnerer map
    public Map<String, SkillMarketDTO> getStatsBySlugs(Collection<String> slugs) {
        if (slugs == null || slugs.isEmpty()) return Collections.emptyMap();
        String url = uri(joinSlugs(slugs));
        ResponseDTO res = fetchTools.getFromApi(url, ResponseDTO.class);
        if (res == null || res.getData() == null) return Collections.emptyMap();

        Map<String, SkillMarketDTO> map = new HashMap<>();
        for (SkillMarketDTO d : res.getData()) {
            if (d.getSlug() != null) map.put(d.getSlug().toLowerCase(), d);
        }
        return map;
    }

    private static String uri(String slugsCsv) {
        return "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + slugsCsv;
    }

    private static String joinSlugs(Collection<String> slugs) {
        return slugs.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.joining(","));
    }

    //DTOs fra ekstern API

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ResponseDTO {
        private SkillMarketDTO[] data;
    }

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class SkillMarketDTO {
        private String id;
        private String slug;
        private String name;
        private String categoryKey;
        private String description;
        private Integer popularityScore;
        private Integer averageSalary;
        private String updatedAt;
    }
}
