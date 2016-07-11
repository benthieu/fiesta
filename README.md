<h1>Fiesta Project</h1>
<h2>Common bugs/fixes for android studio:</h2>
<p>
Add every bugfix in this file, to help others who may run into the same problems
</p>
<ul>
  <li>
  gradle, Could not expand ZIP appcompat-v7:19.0.1 (Benjamin Mathieu 11.07.2016)
    <ul>
      <li>Run android studio as administrator</li>
      <li>Change folder permissions of the project
        <ul>
          <li>Windows: go to folder settings: add everyone -> full rights</li>
          <li>Linux/OSX: chmod 777 'folder'</li>
        </ul>
      </li>
      <li>Gradle sync afterwards</li>
    </ul>
  </li>
</ul>
