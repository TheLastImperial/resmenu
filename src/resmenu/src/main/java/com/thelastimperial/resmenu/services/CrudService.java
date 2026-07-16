package com.thelastimperial.resmenu.services;

public interface CrudService<T, RQ, ID> {
    public T create(RQ rq);
    public T get(ID id);
    public T update(RQ rq, ID id);
    public void delete(ID id);
}
