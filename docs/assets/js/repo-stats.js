document$.subscribe(async () => {
    const url = 'https://codeberg-stats-proxy.vercel.app/repo'
    
    const release_data = await fetch(`${url}/latest-release`)
        .then(_ => _.json());
    const repo_data = await fetch(`${url}/stats`)
        .then(_ => _.json());
    
    console.log(release_data);
    console.log(repo_data);
    
    const list = document.querySelectorAll('ul[data-md-component="codeberg-stats"]');
    
    list.forEach((entry) => {
        entry.innerHTML = `<li class="md-source__fact md-source__fact--version">${release_data.name}</li>
            <li class="md-source__fact md-source__fact--stars">${repo_data.stars}</li>
            <li class="md-source__fact md-source__fact--forks">${repo_data.forks}</li>`
    })
})