import React from "react";

export default function Home() {
  return (
    <div className="min-h-screen bg-white text-slate-900 flex flex-col">
      {/* ヒーローセクション */}
      <header className="flex-1 flex flex-col items-center justify-center text-center px-6">
        <h1 className="text-5xl md:text-6xl font-extrabold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 drop-shadow">
          営業進捗管理アプリ
        </h1>
        <p className="text-lg md:text-xl text-slate-600 max-w-2xl">
          顧客管理から成約まで、チーム全体の営業活動をスマートに可視化する
          ダッシュボード型アプリ。
        </p>
        <div className="mt-8 flex gap-4">
          <button className="px-6 py-3 rounded-xl bg-indigo-600 text-white hover:bg-indigo-700 shadow-lg transition">
            今すぐ始める
          </button>
          <button className="px-6 py-3 rounded-xl bg-slate-200 text-slate-700 hover:bg-slate-300 shadow-lg transition">
            詳細を見る
          </button>
        </div>
      </header>

      {/* サマリーカード */}
      <section className="bg-white py-16 px-6 md:px-16">
        <h2 className="text-3xl font-extrabold text-center mb-12 bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-cyan-500">
          営業サマリー
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 max-w-6xl mx-auto">
          <div className="bg-white rounded-2xl p-6 shadow-lg border border-slate-100 hover:shadow-xl hover:scale-105 transition">
            <h3 className="text-green-600 text-sm">顧客数</h3>
            <p className="text-3xl font-bold mt-2 text-slate-900">128</p>
            <span className="text-green-600 text-sm">▲12% 先月比</span>
          </div>
          <div className="bg-white rounded-2xl p-6 shadow-lg border border-slate-100 hover:shadow-xl hover:scale-105 transition">
            <h3 className="text-yellow-600 text-sm">商談中</h3>
            <p className="text-3xl font-bold mt-2 text-slate-900">42</p>
            <span className="text-yellow-600 text-sm">進行中の案件</span>
          </div>
          <div className="bg-white rounded-2xl p-6 shadow-lg border border-slate-100 hover:shadow-xl hover:scale-105 transition">
            <h3 className="text-green-600 text-sm">成約数</h3>
            <p className="text-3xl font-bold mt-2 text-slate-900">19</p>
            <span className="text-green-600 text-sm">今月</span>
          </div>
          <div className="bg-white rounded-2xl p-6 shadow-lg border border-slate-100 hover:shadow-xl hover:scale-105 transition">
            <h3 className="text-indigo-600 text-sm">成約率</h3>
            <p className="text-3xl font-bold mt-2 text-slate-900">45%</p>
            <span className="text-indigo-600 text-sm">目標まで +10%</span>
          </div>
        </div>
      </section>

      {/* フッター */}
      <footer className="py-6 text-center text-slate-500 text-sm">
        © 2025 営業進捗管理アプリ. All rights reserved.
      </footer>
    </div>
  );
}
