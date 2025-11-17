package com.mipt.mikhaildubov.data_service;

import java.util.Optional;

interface DataService {

  Optional<String> findDataByKey(String key);

  void saveData(String key, String data);

  boolean deleteData(String key);
}
