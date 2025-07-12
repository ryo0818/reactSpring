import { useEffect, useState } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import ClientList from './components/ClientList';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from '../pages/Home';
import Contact from '../pages/Contact';

function App() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8080/api/hello')
      .then((res) => setMessage(res.data))
      .catch((err) => {
        console.error(err);
        setMessage('Error fetching message');
      });
  }, []);

  return (
    <>
    <div className="flex flex-col min-h-screen bg-gray-900 ">
      
      <Header />
      <main className="flex-1 p-4 pt-20 max-w-5xl mx-auto">
        <h1 className="text-3xl font-bold mb-6 flex items-center gap-2 text-gray-100">
          <span role="img" aria-label="phone">📞</span>
          {message || 'Loading...'}</h1>
        <ClientList />
      </main>
      <Footer />
          <Router>
      <Header />
      <nav className="bg-gray-100 p-4 flex gap-4">
        <Link to="/" className="text-blue-500">ホーム</Link>
        <Link to="/contact" className="text-blue-500">お問い合わせ</Link>
        
      </nav>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/contact" element={<Contact />} />
      </Routes>
    </Router>
    </div>
    </>
    
  );
}

export default App;
