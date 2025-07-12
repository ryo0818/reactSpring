import { useEffect, useState } from 'react';
import axios from 'axios';
import ClientList from './components/ClientList';
import Header from './components/Header';
import Footer from './components/Footer';

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
    </div>
    </>
    
  );
}

export default App;
