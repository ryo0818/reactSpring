package com.example.demo.presentation.Debug;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile({
	"local", "dev" }) // 本番では有効化されないように
@RequestMapping("/__debug/db")
public class DbDebugController {

	private final JdbcTemplate jdbc;

	public DbDebugController(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	/** 接続先の基本情報＋対象テーブルの存在確認 */
	@GetMapping
	public Map<String, Object> info() {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("database", jdbc.queryForObject("select current_database()", String.class));
		m.put("schema", jdbc.queryForObject("select current_schema()", String.class));
		m.put("user", jdbc.queryForObject("select current_user", String.class));
		m.put("search_path", jdbc.queryForObject("show search_path", String.class));
		m.put("has_mycompany", jdbc.queryForObject("select to_regclass('public.mycompany')", String.class)); // nullなら無い
		m.put("version", jdbc.queryForObject("select version()", String.class));
		return m;
	}

	/** ざっとテーブル一覧（ユーザー系のみ） */
	@GetMapping("/tables")
	public List<Map<String, Object>> tables() {
		return jdbc.queryForList("""
			  select schemaname, tablename
			  from pg_tables
			  where schemaname not in ('pg_catalog','information_schema')
			  order by schemaname, tablename
			""");
	}
}
