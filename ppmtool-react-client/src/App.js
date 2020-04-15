import React from 'react';
import './App.css';
import Dashboard from './components/Dashboard';
import Header from './components/Layout/Header';
import {BrowserRouter as Router,Route} from "react-router-dom";
import AddProject from './components/project/AddProject';
import {Provider} from "react-redux";
import store from "./store";
import UpdateProject from './components/project/UpdateProject';
import ProjectBoard from './components/projectBoard/ProjectBoard';
import AddProjectTask from './components/projectBoard/ProjectTasks/AddProjectTask';
import UpdateProjectTask from './components/projectBoard/ProjectTasks/UpdateProjectTask';


function App() {
  return (
    <Provider store={store}>  {/* The Redux */}
    <Router>    {/* Rotouting our application */}
    <div className="App">
    <Header />
    <Route exact path="/dashboard" component={Dashboard} /> {/* Providing our app the needed routes of components */}
    <Route exact path="/addProject" component={AddProject} />
    <Route exact path="/updateProject/:id" component={UpdateProject} />
    <Route exact path="/projectBoard/:id" component={ProjectBoard} />
    <Route exact path="/addProjectTask/:id" component={AddProjectTask}/>
    <Route exact path="/updateProjectTask/:backlog_id/:pt_id" component={UpdateProjectTask}/>
    </div>
    </Router>
    </Provider>
  );
}

export default App;
