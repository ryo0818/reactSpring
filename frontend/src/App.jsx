import { useEffect, useState } from "react";
import axios from "axios";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import ClientList from "./components/ClientList";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "../pages/Home";
import Contact from "../pages/Contact";

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/hello")
      .then((res) => setMessage(res.data))
      .catch((err) => {
        console.error(err);
        setMessage("Error fetching message");
      });
  }, []);

  return (
    <div className="min-h-screen flex flex-col ">
    <Router>
      <Header />
      <Footer />
    </Router>
    </div>
  );
}

export default App;
