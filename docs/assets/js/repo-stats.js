document$.subscribe(async () => {
    const url = 'https://codeberg-stats-proxy.vercel.app/repo'
    const repo_stats = document.querySelector('[data-md-component="source"] .md-source__repository');
    
    function loadInfo(data) {
        const facts = document.createElement("ul");
        facts.className = "md-source__facts";
        
        const version = document.createElement("li");
        version.className = "md-source__fact md-source__fact--version";
        
        const stars = document.createElement("li");
        stars.className = "md-source__fact md-source__fact--stars";
        
        const forks = document.createElement("li");
        forks.className = "md-source__fact md-source__fact--forks";
        
        version.innerText = data["version"];
        stars.innerText = data["stars"];
        forks.innerText = data["forks"];
        
        facts.appendChild(version);
        facts.appendChild(stars);
        facts.appendChild(forks);
        
        repo_stats.appendChild(facts);
    }
    
    async function fetchInfo() {
        const [release, repo] = await Promise.all([
            fetch(`${url}/latest-release`).then(_ => _.json()),
            fetch(`${url}/stats`).then(_ => _.json()),
        ]);
        
        const data = {
            "version": release.name,
            "stars": repo.stars,
            "forks": repo.forks
        };
        
        __md_set("__git_repo", data, sessionStorage);
        loadInfo(data);
    }
    
    if(!document.querySelector('[data-md-component="source"] .md-source__facts')){
        const cached = __md_get("__git_repo", sessionStorage);
        if((cached != null) && (cached["version"])) {
            loadInfo(cached);
        } else {
            fetchInfo();
        }
    }
})