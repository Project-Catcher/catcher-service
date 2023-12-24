package com.catcher.datasource.user;

import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.UserSearchFilterType;
import com.catcher.core.domain.entity.User;
import com.catcher.infrastructure.utils.CriteriaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public void saveAll(List<User> userList) {
        userJpaRepository.saveAll(userList);
    }

    @Override
    public Long countByDeletedAtIsNotNull() {
        return userJpaRepository.countByDeletedAtIsNotNull();
    }

    @Override
    public Long count() {
        return userJpaRepository.count();
    }


    /**
     * startDate 부터 endDate 까지의 날짜를 key로 하고 0L을 value로 갖는 Map을 생성한다.
     */
    private Map<String, Long> initMap(final LocalDate startDate, final LocalDate endDate) {
        Map<String, Long> dateMap = new LinkedHashMap<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateMap.put(date.toString(), 0L);
        }

        return dateMap;
    }

    /**
     * startDate 부터 endDate 까지의 날짜를 key로 하고 해당 날짜의 count를 쿼리 결과로 부터 가져와서 value에 넣는 Map을 생성한다.
     */
    private Map<String, Long> returnMap(final LocalDate startDate, final LocalDate endDate, GroupedByDateQueryFunction groupedByDateQueryFunction) {
        final var resultMap = initMap(startDate, endDate);
        final List<Map<String, Object>> queryResults = groupedByDateQueryFunction.apply(startDate.atStartOfDay(ZoneId.systemDefault()), endDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()));

        queryResults.forEach(result -> {
            resultMap.put(result.get("date").toString(), (Long) result.get("count"));
        });

        return resultMap;
    }

    @FunctionalInterface
    public interface GroupedByDateQueryFunction {
        List<Map<String, Object>> apply(ZonedDateTime start, ZonedDateTime end);
    }

    @Override
    public Map<String, Long> countNewUsersPerDay(final LocalDate startDate, final LocalDate endDate) {
        return returnMap(startDate, endDate, userJpaRepository::countNewUsersPerDay);
    }

    @Override
    public Map<String, Long> countDeletedUsersPerDay(final LocalDate startDate, final LocalDate endDate) {
        return returnMap(startDate, endDate, userJpaRepository::countDeletedUsersPerDay);
    }

    @Override
    public Map<String, Long> countReportedUsersPerDay(final LocalDate startDate, final LocalDate endDate) {
        return returnMap(startDate, endDate, userJpaRepository::countReportedUsersPerDay);
    }

    @Override
    public Page<User> searchUsersWithFilter(final UserSearchFilterType filterType, final LocalDate startDate, final LocalDate endDate, final String query, final Pageable pageable) {
        Specification<User> specification = CriteriaUtil.generateQueryBuilder(filterType, startDate, endDate, query);

        return userJpaRepository.findAll(specification, pageable);
    }

}
