document$.subscribe(async () => {
    const url = 'https://codeberg-stats-proxy.vercel.app/repo'
    
    const [release, repo] = await Promise.all([
        fetch(`${url}/latest-release`).then(_ => _.json()),
        fetch(`${url}/stats`).then(_ => _.json()),
    ]);
    
    const list = document.querySelectorAll('ul[data-md-component="codeberg-stats"]');
    
    list.forEach((entry) => {
        entry.innerHTML = `<li class="md-source__fact md-source__fact--version">${release.name}</li>
            <li class="md-source__fact md-source__fact--stars">${repo.stars}</li>
            <li class="md-source__fact md-source__fact--forks">${repo.forks}</li>`
    })
})