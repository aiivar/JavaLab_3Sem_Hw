package ru.itis.aivar.repositories;

import ru.itis.aivar.models.CookieMock;

public interface CookiesRepository extends CrudRepository<CookieMock>{

    CookieMock findByUUID(String uuid);

}
