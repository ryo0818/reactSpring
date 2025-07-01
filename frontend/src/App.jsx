import { useEffect, useState } from 'react';
import axios from 'axios';

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
    <div>
      <h1>{message || 'Loading...'}</h1>
    </div>
  );
}

export default App;
