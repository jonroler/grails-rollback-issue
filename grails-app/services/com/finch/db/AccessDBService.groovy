package com.finch.db;

import grails.transaction.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Transactional
public class AccessDBService {
  @Resource(name = "dataSource")
  private DataSource dataSource;

  public int getNumFoos() throws Exception {
    Connection connection = dataSource.getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from foo");
      try {
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
          if (resultSet.next()) {
            return resultSet.getInt(1);
          }
          throw new RuntimeException("Failed to get foo count.");
        } finally {
          resultSet.close();
        }
      } finally {
        preparedStatement.close();
      }
    } finally {
      connection.close();
    }
  }

  public void insertFoo() throws Exception {
    Connection connection = dataSource.getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("insert into foo(bar) values(?)");
      try {
        preparedStatement.setInt(1, 3);
        preparedStatement.executeUpdate();
      } finally {
        preparedStatement.close();
      }
    } finally {
      connection.close();
    }
  }
}
