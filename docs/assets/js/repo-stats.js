document$.subscribe(async () => {
    const url = 'https://codeberg-stats-proxy.vercel.app'
    const repo_stats = document.querySelector('[data-md-component="source"] .md-source__repository');
    
    function loadCodebergInfo(data) {
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
    
    function loadApiInfo(data) {
        const version = data["version"];
        const versionToken = '{apiVersion}';
        const codeBlocks = document.querySelectorAll('.md-content pre code');
        for(const codeBlock of codeBlocks) {
            codeBlock.innerHTML = codeBlock.innerHTML.replace(new RegExp(versionToken, 'g'), version.substring(1));
        }
    }
    
    function loadPluginVersion(data) {
        const version = data["version"];
        const versionToken = '{version}'
        const entries = document.querySelectorAll('.md-content code');
        for(const entry of entries) {
            entry.innerHTML = entry.innerHTML.replace(new RegExp(versionToken, 'g'), version.substring(1));
        }
    }
    
    async function fetchApiInfo() {
        const tag = await fetch(`${url}/api/latest-release`).then(_ => _.json());
        
        const data = {
            "version": tag.name
        };
        
        __md_set("__api_tag", data, sessionStorage);
        loadApiInfo(data);
    }
    
    async function fetchInfo() {
        const [release, repo] = await Promise.all([
            fetch(`${url}/repo/latest-release`).then(_ => _.json()),
            fetch(`${url}/repo/stats`).then(_ => _.json()),
        ]);
        
        const data = {
            "version": release.name,
            "stars": repo.stars,
            "forks": repo.forks
        };
        
        __md_set("__git_repo", data, sessionStorage);
        loadCodebergInfo(data);
        loadPluginVersion(data);
    }
    
    if(!document.querySelector('[data-md-component="source"] .md-source__facts')) {
        const cached = __md_get("__git_repo", sessionStorage);
        if((cached != null) && (cached["version"])) {
            loadCodebergInfo(cached);
            loadPluginVersion(cached);
        } else {
            fetchInfo();
        }
    }
    
    if(location.href.includes('/api/')) {
        const cachedApi = __md_get("__api_tag", sessionStorage);
        if((cachedApi != null) && (cachedApi["version"])) {
            loadApiInfo(cachedApi);
        } else {
            fetchApiInfo();
        }
    }
})