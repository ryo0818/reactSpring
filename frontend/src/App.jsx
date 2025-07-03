import { useEffect, useState } from 'react';
import axios from 'axios';
import ClientList from './components/ClientList';

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
    
    <div className="App">
      <h1>{message || 'Loading...'}</h1>
      <ClientList />
    </div>
  );
}

export default App;
