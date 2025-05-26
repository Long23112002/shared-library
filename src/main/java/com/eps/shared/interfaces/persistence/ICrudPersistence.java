package com.eps.shared.interfaces.persistence;

public interface ICrudPersistence<E, ID>
    extends ICreatePersistence<E, ID>,
        IUpdatePersistence<E, ID>,
        IDeletePersistence<E, ID>,
        IGetPersistence<E, ID> {}
