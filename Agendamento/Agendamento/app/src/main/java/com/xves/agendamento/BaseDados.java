package com.xves.agendamento;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDados extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "agendamento_db";
	private static final int DATABASE_VERSION = 12;

	public BaseDados(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String sql = "CREATE TABLE xves_user ( " +
					"   id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,    " +
					"   nome TEXT ,                " +
					"   cpf TEXT ,                " +
					"   fone TEXT ,                " +
					"   preco TEXT ,            " +
					"   senha TEXT ,            " +
					"   tipo TEXT ,            " +
					"   endereco TEXT);";

			db.execSQL(sql);

			sql = "CREATE TABLE xves_user_specialty (    " +
					"   id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,       " +
					"   id_usuario TEXT ,                 " +
					"   id_especialidade TEXT ,             " +
					"   descricao TEXT);";

			db.execSQL(sql);

			sql = "CREATE TABLE xves_service (          " +
					"   id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,       " +
					"   id_cliente TEXT ,          " +
					"   id_profissional TEXT ,     " +
					"   endereco TEXT ,             " +
					"   data TEXT ,                  " +
					"   hora TEXT ,                  " +
					"   valor TEXT ,                " +
					"   status TEXT ,               " +
					"   descricao TEXT);";

			db.execSQL(sql);

			StringBuilder cli1 = new StringBuilder();
			cli1.append("INSERT INTO xves_user VALUES (1, 'Lucia', '11111111111', '(11)999999999', 'R$ 150.00', '123', 'P', 'Rua teste1, curitiba-PR');");
			db.execSQL(cli1.toString());

			StringBuilder cli2 = new StringBuilder();
			cli2.append("INSERT INTO xves_user VALUES (2, 'Ana', '22222222222', '(11)999999999', 'R$ 100.00', '123', 'P', 'Rua teste2, curitiba-PR');");
			db.execSQL(cli2.toString());

			StringBuilder cli3 = new StringBuilder();
			cli3.append("INSERT INTO xves_user VALUES (3, 'Maria', '33333333333', '(11)999999999', 'R$ 300.00', '123', 'P', 'Rua teste3, curitiba-PR');");
			db.execSQL(cli3.toString());

			StringBuilder esp1 = new StringBuilder();
			esp1.append("INSERT INTO xves_user_specialty VALUES (1, '2', '1','Drenagem Linfática');");
			db.execSQL(esp1.toString());

			StringBuilder esp2 = new StringBuilder();
			esp2.append("INSERT INTO xves_user_specialty VALUES (2, '3', '2','Depilação');");
			db.execSQL(esp2.toString());

			StringBuilder esp3 = new StringBuilder();
			esp3.append("INSERT INTO xves_user_specialty VALUES (3, '1', '3','Fatslin');");
			db.execSQL(esp3.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			String sql = "DROP TABLE IF EXISTS xves_user;";
			db.execSQL(sql);

			sql = "DROP TABLE IF EXISTS xves_user_specialty;";
			db.execSQL(sql);

			sql = "DROP TABLE IF EXISTS xves_service;";
			db.execSQL(sql);

			onCreate(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UserModel getClienteData(String idCliente) {
		SQLiteDatabase db = this.getReadableDatabase();
		UserModel userModel = new UserModel();

		try {

			if (idCliente != null) {
				Cursor cursor = db.rawQuery("SELECT * FROM xves_user WHERE id = ?", new String[]{idCliente});
				if (cursor.moveToFirst()) {
					userModel.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
					userModel.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
					userModel.setCpf(cursor.getString(cursor.getColumnIndexOrThrow("cpf")));
					userModel.setFone(cursor.getString(cursor.getColumnIndexOrThrow("fone")));
				}

				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return userModel;
	}
}
