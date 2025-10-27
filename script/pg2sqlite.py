import sqlite3
import psycopg2
import json


def execute1():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from browse_history;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2], row[4]))

    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into browse_history values(?,?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

def execute2():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from download_history;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2], row[4], row[5]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into download_history values(?,?,?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

def execute3():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from data_relational;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into data_relational values(?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

def execute4():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from data_list;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], json.dumps(row[2]), row[3], json.dumps(row[4], ensure_ascii=False), row[6], row[7], row[8], row[9], row[10], row[12], row[13], row[14], row[15], row[16], row[17], row[19], row[20]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into data_list values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()


def execute5():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from files;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2], row[3], row[5], row[6], row[7]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into files values(?,?,?,?,?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

def execute6():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from visual_file;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2], row[3], row[4]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into visual_file values(?,?,?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

def execute7():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from analytic_parameter;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into analysis_parameter values(?,?,?,?,?,?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

def execute8():
    conn = psycopg2.connect(user="postgres", password="123",
                            dbname="dataset", host="localhost", port="5432")
    cursor = conn.cursor()
    cursor.execute("select * from folder;")
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    arr = []
    for row in rows:
        arr.append((row[0], row[1], row[2]))
    conn_sqlite = sqlite3.connect("E:/monitor/main.db")
    cur_sqlite = conn_sqlite.cursor()
    cur_sqlite.executemany("insert into folder values(?,?,?)", arr)
    conn_sqlite.commit()
    cur_sqlite.close()
    conn_sqlite.close()

execute5()
